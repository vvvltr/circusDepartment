namespace hh.Domain;

public class VacanciesIdsResponseDto
{
    public List<Item> Items { get; set; }
    public class Item
    {
        public int Id { get; set; }
    }
}