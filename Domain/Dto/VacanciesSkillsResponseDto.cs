namespace hh.Domain;

public class VacanciesSkillsResponseDto
{
    public List<Item> Items { get; set; }
    public class Item
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public List<string> Skills { get; set; }
    }
    public List<string> Skills { get; set; }
}