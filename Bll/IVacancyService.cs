using hh.Domain;
using Microsoft.AspNetCore.Mvc;

namespace hh.Bll;

public interface IVacancyService
{
    Task<VacanciesIdsResponseDto> GetListAsync(Dictionary<string, string> queryParams);

    Task<List<VacanciesInfoResponseDto>> GetInfoAsync(Dictionary<string, string> vacancies);

    Task<VacanciesSkillsResponseDto> GetVacanciesSkillsList(SkillCheckResponseDto skillCheckResponseDto);

    Task<SkillCheckResponseDto?> GetSkillcheck(Dictionary<string, string> queryParams);
}