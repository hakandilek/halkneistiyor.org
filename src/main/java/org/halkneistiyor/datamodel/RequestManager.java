package org.halkneistiyor.datamodel;

/**
 * @author Erdinc Yilmazel (eyilmazel@tripadvisor.com)
 * @since 6/6/13
 */
public interface RequestManager
{
    /**
     * Adds a new request to the data backend
     * @param request The request entry
     * @return Returns the data store id of the request that was stored
     * @throws Exception
     */
    public String addRequest(RequestEntry request) throws Exception;

    public RequestEntry getRequest(String requestId) throws Exception;

    public RequestEntry getRequestByUrlId(String urlId) throws Exception;

    /**
     * Sets the vote for a user.
     *
     * This method either inserts a new or updates an existing vote of a user
     * for a specific request. It also is supposed to update the yes/no counters
     * for the request.
     *
     * @param userId Id (data store userId) of the user
     * @param requestId Id (data store userId) of the request
     * @param accept Yes or no
     * @return Returns the data store id of the vote that was stored.
     */
    public String registerVote(String userId, String requestId, boolean accept) throws Exception;

    /**
     * Returns the existing vote (if any) of a specific user for a specific request.
     * @param userId Id (data store userId) of the user
     * @param requestId Id (data store userId) of the request
     * @return The vote of the user for the request or null if there isn't a vote yet.
     * @throws Exception
     */
    public Vote getVote(String userId, String requestId) throws Exception;
}
