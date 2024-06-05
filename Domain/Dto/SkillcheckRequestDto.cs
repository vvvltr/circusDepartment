namespace hh.Domain;

public class SkillcheckRequestDto
{
    public List<Item> Items { get; set; }
    public class Item
    {
        public int Id { get; set; }
        public string Description { get; set; }
    }
    
}