namespace hh.Domain;

public class VacanciesListResponseDto
{
    public List<Item> Items { get; set; }
}

public class Item
{
    public string Id { get; set; }
    public string Name { get; set; }
}