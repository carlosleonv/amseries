package com.example.gae;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore.IClusteredPageStore;
import org.apache.wicket.protocol.http.pagestore.AbstractPageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;

/**
 * @author eeugene
 * 
 */
public class CachePageStore extends AbstractPageStore implements IClusteredPageStore {

    private Logger logger = LoggerFactory.getLogger(CachePageStore.class);

    private Set<String> map = new HashSet<String>();

    private Cache cache;

    // Property map
    private Map props = new HashMap();

    public CachePageStore() {
	try {
	    props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
	    props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true);
	    CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
	    cache = cacheFactory.createCache(props);
	    if (logger.isDebugEnabled()) {
		logger.debug("Cache instancied");
	    }
	} catch (CacheException e) {
	    logger.error("CacheException: " + e.getMessage());
	    return;
	}
    }

    @Override
    public boolean containsPage(String sessionId, String pageMapName, int pageId, int pageVersion) {
	String getKey = getKey(sessionId, pageMapName, pageId, pageVersion);
	boolean containsPage = cache.containsKey(getKey);
	if (logger.isDebugEnabled()) {
	    logger.debug("########## containsPage, getKey = " + getKey + " -> " + (containsPage ? "oui" : "non"));
	}
	return containsPage;
    }

    @Override
    public void destroy() {
	cache.clear();
	map.clear();
	if (logger.isDebugEnabled()) {
	    logger.debug("########## destroy, cache.size = " + cache.size() + " map.size = " + map.size());
	}
    }

    @Override
    public <T> Page getPage(String sessionId, String pagemap, int id, int versionNumber, int ajaxVersionNumber) {
	return (Page) cache.get(getKey(sessionId, pagemap, id, versionNumber, ajaxVersionNumber));
    }

    @Override
    public void removePage(String sessionId, String pagemap, int id) {
	String key = getKey(sessionId, pagemap, id, 0);
	key = key.substring(0, key.lastIndexOf(":"));

	Iterator<String> it = map.iterator();
	while (it.hasNext()) {
	    String entry = it.next();
	    if (entry.startsWith(key)) {
		cache.remove(entry);
		it.remove();
	    }
	}
	if (logger.isDebugEnabled()) {
	    logger.debug("########## removePage, sessionId = " + sessionId + ", cache.size = " + cache.size()
		    + " map.size = " + map.size());
	}
    }

    @Override
    public void storePage(String sessionId, Page page) {
	String storeKey = storeKey(sessionId, page);
	cache.put(storeKey, page);
	map.add(storeKey);
	if (logger.isDebugEnabled()) {
	    logger.debug("########## storePage, sessionId = " + sessionId + ", cache.size = " + cache.size()
		    + " map.size = " + map.size());
	}
    }

    @Override
    public void unbind(String sessionId) {
	Iterator<String> it = map.iterator();
	while (it.hasNext()) {
	    String key = it.next();
	    if (key.startsWith(sessionId)) {
		cache.remove(key);
		it.remove();
	    }
	}
	if (logger.isDebugEnabled()) {
	    logger.debug("########## unbind, sessionId = " + sessionId + ", cache.size = " + cache.size()
		    + " map.size = " + map.size());
	}
    }

    public String storeKey(final String sessionId, final Page page) {
	String key = sessionId + ":" + page.getPageMapName() + ":" + page.getId() + ":"
		+ page.getCurrentVersionNumber() + ":" + (page.getAjaxVersionNumber() - 1);
	if (logger.isDebugEnabled()) {
	    logger.debug("########## storeKey: " + key);
	}
	return key;
    }

    public String getKey(final String sessId, final String pageMapName, final int pageId, final int pageVersion) {
	return getKey(sessId, pageMapName, pageId, pageVersion, -1);
    }

    public String getKey(final String sessId, final String pageMapName, final int pageId, final int pageVersion,
	    final int ajaxVersion) {
	String key = sessId + ":" + pageMapName + ":" + pageId + ":" + pageVersion + ":" + ajaxVersion;
	return key;
    }

    @Override
    public void pageAccessed(String sessionId, Page page) {
	// TODO Auto-generated method stub
    }
}
