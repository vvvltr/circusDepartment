using System.Text;
using System.Text.Json;
using System.Web;
using hh.Domain;
using hh.Helper;
using Microsoft.AspNetCore.Mvc;

namespace hh.Bll;

public class VacancyService : IVacancyService
{
    private readonly string _baseUri = "https://api.hh.ru/vacancies";
    private readonly string _pyUri = "http://164.92.162.137:5000/skills";
    
    private readonly HttpClient _httpClient;
    private readonly ILogger<VacancyService> _logger;
    private readonly IConfiguration _configuration;
    public VacancyService(HttpClient httpClient, 
        ILogger<VacancyService> logger,
        IConfiguration configuration)
    {
        _httpClient = httpClient ?? throw new ArgumentException(nameof(httpClient));
        _logger = logger ?? throw new ArgumentException(nameof(logger));
        _configuration = configuration ?? throw new ArgumentException(nameof(configuration));
    }
    
    public async Task<VacanciesIdsResponseDto> GetListAsync(Dictionary<string, string> queryParams)
    {
        /*var uri = QueryParser.ParseQuery(queryParams, _baseUri).ToString();
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
        }*/
        // получить ответ с хх.ру
        var response = await GetListFromHeadHunter(queryParams);
        // сериализация ответа в джсон структуру
        var vac = await response.Content.ReadFromJsonAsync<VacanciesIdsResponseDto>();
        return vac;
    }
    
    public async Task<List<VacanciesInfoResponseDto>> GetInfoAsync(Dictionary<string, string> queryParams)
    {
        // получить ответ с хх.ру
        var apiResponse = await GetListFromHeadHunter(queryParams);
        // сериализация в список вакансий
        var vacancies = await apiResponse.Content.ReadFromJsonAsync<VacanciesIdsResponseDto>();
        var list = new List<VacanciesInfoResponseDto>();
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

            var vacancyRespond = await response.Content.ReadFromJsonAsync<VacanciesInfoResponseDto>();
            list.Add(vacancyRespond);
        }

        return list;
    }

    public async Task<SkillCheckResponseDto?> GetSkillcheck(Dictionary<string, string> queryParams)
    {
        // получить ответ с хх.ру
        var apiResponse = await GetListFromHeadHunter(queryParams);
        // сериализация в список id and description
        var vacancies = await apiResponse.Content.ReadFromJsonAsync<VacanciesIdsResponseDto>();
        var skillcheckRequestContent = new SkillcheckRequestDto();
        skillcheckRequestContent.Items = new List<SkillcheckRequestDto.Item>();
        foreach (var vacancy in vacancies.Items)
        {
            var vacancyInfoResponse = await GetInfoFromHeadHunter(vacancy.Id);

            var vacancyInfo = await vacancyInfoResponse.Content.ReadFromJsonAsync<SkillcheckRequestDto.Item>();
            skillcheckRequestContent.Items.Add(new SkillcheckRequestDto.Item()
            {
                Id = vacancyInfo.Id,
                Description = vacancyInfo.Description
            });
        }
        
        // запрос к python api
        var uri = $"{_configuration["PythonApi:BaseUrl"]}";
        var request = new HttpRequestMessage(HttpMethod.Post, uri);
        var content = JsonSerializer.Serialize(skillcheckRequestContent);
        request.Content = new StringContent(content, Encoding.UTF8, "application/json");
        HttpResponseMessage response = null;
        try
        {
            response = await _httpClient.SendAsync(request);
            response.EnsureSuccessStatusCode();

        }
        catch (HttpRequestException e)
        {
            _logger.LogError(e, "error sending data to python api");
            throw new ArgumentException(nameof(e));
        }

        var str = await response.Content.ReadAsStringAsync();
        return await response.Content.ReadFromJsonAsync<SkillCheckResponseDto>();
    }

    public async Task<VacanciesSkillsResponseDto> GetVacanciesSkillsList(SkillCheckResponseDto skillcheck)
    {
        VacanciesSkillsResponseDto vacanciesSkillsDto = new VacanciesSkillsResponseDto();
        vacanciesSkillsDto.Items = new List<VacanciesSkillsResponseDto.Item>();
        vacanciesSkillsDto.Skills = new List<string>();
        vacanciesSkillsDto.Skills = skillcheck.skills;
        foreach (var vacancy in skillcheck.Items)
        {
            var responseMessage = await GetInfoFromHeadHunter(vacancy.Id);
            var vacancyInfo = await responseMessage.Content.ReadFromJsonAsync<VacanciesInfoResponseDto>();
            vacanciesSkillsDto.Items.Add(new VacanciesSkillsResponseDto.Item()
            {
                Id = vacancy.Id,
                Description = vacancyInfo.Description,
                Name = vacancyInfo.Name,
                Skills = vacancy.skills
            });
        }

        return vacanciesSkillsDto;
    }

    // возвращает httpResponseMessage от апи хх.ру
    private async Task<HttpResponseMessage> GetListFromHeadHunter(Dictionary<string, string> queryParams)
    {
        var uri = QueryParser.ParseQuery(queryParams, _baseUri).ToString();
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

        return response;
    }
// возвращает ответ с хх.ру с полной информацией по вакансии
    private async Task<HttpResponseMessage> GetInfoFromHeadHunter(int id)
    {
        var uri = _baseUri + $"/{id}";
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

        return response;
    }
}