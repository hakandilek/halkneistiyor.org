package org.halkneistiyor.datamodel.gae;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.halkneistiyor.datamodel.RequestEntry;

import java.util.Date;


/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/8/13
 */
public class RequestEntityBuilder
{
    public static final String REQUESTER_ID = "requesterId";
    public static final String ENTRY_DATE = "entryDate";
    public static final String URL_ID = "urlId";
    public static final String REQUEST = "request";
    public static final String YES_COUNT = "yesCount";
    public static final String NO_COUNT = "noCount";

    public static Entity getEntity(RequestEntry requestEntry)
    {
        Entity entity;

        if (requestEntry.getRequesterId() == null)
        {
            entity = new Entity(RequestEntry.KIND);
        }
        else
        {
            entity = new Entity(requestEntry.getRequesterId());
        }

        entity.setProperty(REQUESTER_ID, KeyFactory.stringToKey(requestEntry.getRequesterId()));
        entity.setProperty(ENTRY_DATE, requestEntry.getEntryDate());
        entity.setProperty(URL_ID, requestEntry.getUrlId());
        entity.setProperty(REQUEST, requestEntry.getRequest());
        entity.setProperty(YES_COUNT, requestEntry.getYesCount());
        entity.setProperty(NO_COUNT, requestEntry.getNoCount());

        return entity;
    }

    public static RequestEntry getRequestEntry(Entity entity)
    {
        RequestEntry requestEntry = new RequestEntry();
        Key key = entity.getKey();
        if (key != null)
        {
            requestEntry.setRequesterId(KeyFactory.keyToString(key));
        }

        Key requesterId = (Key) entity.getProperty(REQUESTER_ID);
        if (requesterId != null)
        {
            requestEntry.setRequesterId(KeyFactory.keyToString(requesterId));
        }

        requestEntry.setEntryDate((Date) entity.getProperty(ENTRY_DATE));
        requestEntry.setUrlId((String) entity.getProperty(URL_ID));
        requestEntry.setRequest((String) entity.getProperty(REQUEST));
        Object yesCount = entity.getProperty(YES_COUNT);
        requestEntry.setYesCount(yesCount == null ? 0 : (Long) yesCount);
        Object noCount = entity.getProperty(NO_COUNT);
        requestEntry.setNoCount(noCount == null ? 0 : (Long) noCount);

        return requestEntry;
    }
}
