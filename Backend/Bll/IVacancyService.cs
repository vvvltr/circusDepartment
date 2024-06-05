using hh.Domain;
using Microsoft.AspNetCore.Mvc;

namespace hh.Bll;

public interface IVacancyService
{
    Task<VacanciesIdsResponseDto> GetListAsync(Dictionary<string, string> queryParams);

    Task<List<VacanciesInfoResponseDto>> GetInfoListAsync(Dictionary<string, string> vacancies);
    Task<VacanciesInfoResponseDto> GetInfoAsync(int id);

    Task<VacanciesSkillsResponseDto> GetVacanciesSkillsList(SkillCheckResponseDto skillCheckResponseDto);

    Task<SkillCheckResponseDto?> GetSkillcheck(Dictionary<string, string> queryParams);
}