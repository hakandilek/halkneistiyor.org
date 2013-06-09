package org.halkneistiyor.datamodel;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/6/13
 */
public class Vote implements Serializable
{
    public static final String KIND = "Vote";

    String voteId;
    String requestId;
    String userId;
    Date voteDate;
    boolean accept;

    public String getVoteId()
    {
        return voteId;
    }

    public void setVoteId(String voteId)
    {
        this.voteId = voteId;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Date getVoteDate()
    {
        return voteDate;
    }

    public void setVoteDate(Date voteDate)
    {
        this.voteDate = voteDate;
    }

    public boolean isAccepted()
    {
        return accept;
    }

    public void setAccepted(boolean accept)
    {
        this.accept = accept;
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

        Vote vote = (Vote) o;

        return !(voteId != null ? !voteId.equals(vote.voteId) : vote.voteId != null);
    }

    @Override
    public int hashCode()
    {
        return voteId != null ? voteId.hashCode() : 0;
    }
}
