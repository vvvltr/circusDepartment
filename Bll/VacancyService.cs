using System.Text.Json;
using System.Web;
using hh.Domain;
using Microsoft.AspNetCore.Mvc;

namespace hh.Bll;

public class VacancyService : IVacancyService
{
    private readonly string _baseUri = "https://api.hh.ru/vacancies";

    private readonly HttpClient _httpClient;
    private readonly ILogger<VacancyService> _logger;

    public VacancyService(HttpClient httpClient, ILogger<VacancyService> logger)
    {
        _httpClient = httpClient;
        _logger = logger;
    }
    
    public async Task<VacanciesListResponseDto> GetVacanciesAsync(Dictionary<string, string> queryParams)
    {
        var uri = ParseQuery(queryParams).ToString();
        _logger.LogInformation(uri);
        var request = new HttpRequestMessage(HttpMethod.Get, uri);
        request.Headers.Add("User-Agent", "ourStudyProject/1.0");

        HttpResponseMessage response = null;
        try
        {
            response = await _httpClient.SendAsync(request);
            response.EnsureSuccessStatusCode();
        }
        catch (HttpRequestException e)
        {
            _logger.LogError(e, "Error fetching vacancies from hh.ru");
            throw new ArgumentException(nameof(e));
        }

        var vac = await response.Content.ReadFromJsonAsync<VacanciesListResponseDto>();
        return vac;
    }
    
    public async Task<List<VacancyResponseDto>> GetVacanciesDescription(VacanciesListResponseDto vacancies)
    {
        var list = new List<VacancyResponseDto>();
        foreach (var vacancy in vacancies.Items)
        {
            var uri = _baseUri + $"/{vacancy.Id}";
            var request = new HttpRequestMessage(HttpMethod.Get, uri);
            request.Headers.Add("User-Agent", "ourStudyProject/1.0");

            HttpResponseMessage response = null;
            try
            {
                response = await _httpClient.SendAsync(request);
                response.EnsureSuccessStatusCode();
            }
            catch (HttpRequestException e)
            {
                _logger.LogError(e, "Error fetching vacancies from hh.ru");
                throw new ArgumentException(nameof(e));
            }

            var vacancyRespond = await response.Content.ReadFromJsonAsync<VacancyResponseDto>();
            list.Add(vacancyRespond);
        }

        return list;
    }

    private UriBuilder ParseQuery(Dictionary<string, string> queryParams)
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