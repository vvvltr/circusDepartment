using System.Web;

namespace hh.Helper;

public static class QueryParser
{
    public static UriBuilder ParseQuery(Dictionary<string, string> queryParams, string _baseUri)
    {
        var uriBuilder = new UriBuilder(_baseUri);
        var query = HttpUtility.ParseQueryString(uriBuilder.Query);
        foreach (var param in queryParams)
        {
            query[param.Key] = param.Value;
        }

        uriBuilder.Query = query.ToString();
        return uriBuilder;
    }
}