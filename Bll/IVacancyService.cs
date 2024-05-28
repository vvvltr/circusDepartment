using hh.Domain;
using Microsoft.AspNetCore.Mvc;

namespace hh.Bll;

public interface IVacancyService
{
    Task<VacanciesListResponseDto> GetVacanciesAsync(Dictionary<string, string> queryParams);

    Task<List<VacancyResponseDto>> GetVacanciesDescription(VacanciesListResponseDto vacancies);
}