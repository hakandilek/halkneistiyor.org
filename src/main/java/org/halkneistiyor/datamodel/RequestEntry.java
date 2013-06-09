package org.halkneistiyor.datamodel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/6/13
 */
public class RequestEntry implements Serializable
{
    public static final long serialVersionUID = 1L;

    public static final String KIND = "RequestEntry";

    String requestId;
    String requesterId;
    Date entryDate;
    String request;
    String urlId;
    long yesCount;
    long noCount;

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getRequesterId()
    {
        return requesterId;
    }

    public void setRequesterId(String requesterId)
    {
        this.requesterId = requesterId;
    }

    public Date getEntryDate()
    {
        return entryDate;
    }

    public void setEntryDate(Date entryDate)
    {
        this.entryDate = entryDate;
    }

    public String getRequest()
    {
        return request;
    }

    public void setRequest(String request)
    {
        this.request = request;
    }

    public String getUrlId()
    {
        return urlId;
    }

    public void setUrlId(String urlId)
    {
        this.urlId = urlId;
    }

    public long getYesCount()
    {
        return yesCount;
    }

    public void setYesCount(long yesCount)
    {
        this.yesCount = yesCount;
    }

    public long getNoCount()
    {
        return noCount;
    }

    public void setNoCount(long noCount)
    {
        this.noCount = noCount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        RequestEntry that = (RequestEntry) o;

        return !(requestId != null ? !requestId.equals(that.requestId) : that.requestId != null);
    }

    @Override
    public int hashCode()
    {
        return requestId != null ? requestId.hashCode() : 0;
    }
}
