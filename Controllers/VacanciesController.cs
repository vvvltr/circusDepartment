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

        /// <summary>
        /// Возвращает список вакансий + данные о соответствии навыков
        /// </summary>
        /// <param name="queryParams"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<VacanciesSkillsResponseDto> GetSkillCheck([FromQuery] Dictionary<string, string> queryParams)
        {
            var skillcheckDto = await _service.GetSkillcheck(queryParams);
            return await _service.GetVacanciesSkillsList(skillcheckDto);
        }

        /// <summary>
        /// Возвращает список вакансий со всеми параметрами
        /// </summary>
        /// <param name="queryParams"></param>
        /// <returns></returns>
        [HttpGet]
        public async Task<List<VacanciesInfoResponseDto>> GetList([FromQuery] Dictionary<string, string> queryParams)
        {
            return await _service.GetInfoAsync(queryParams);
        }
        
    }
}