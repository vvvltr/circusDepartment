using System.Collections.Specialized;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using System.Web;
using hh.Bll;
using hh.Domain;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace hh.Controllers
{
    [ApiController]
    [Route("[controller]/[action]")]
    [Authorize]
    public class VacanciesController : ControllerBase
    {
        private readonly HttpClient _httpClient;
        private readonly ILogger<VacanciesController> _logger;
        private readonly IVacancyService _service;
        private readonly string _baseUri = "https://api.hh.ru/vacancies";

        public VacanciesController(HttpClient httpClient, ILogger<VacanciesController> logger,
            IVacancyService vacancyService)
        {
            _httpClient = httpClient;
            _logger = logger;
            _service = vacancyService;
        }

        [HttpGet]
        public async Task<VacanciesListResponseDto> GetVacancies([FromQuery] Dictionary<string, string> queryParams)
        {
            return await _service.GetVacanciesAsync(queryParams);
        }

        [HttpGet]
        public async Task<List<VacancyResponseDto>> GetVacanciesDescriprions([FromQuery] Dictionary<string, string> queryParams)
        {
            var resp = await _service.GetVacanciesAsync(queryParams);
            return await _service.GetVacanciesDescription(resp);
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
}