namespace hh.Domain;

public class SkillCheckResponseDto
{
    public List<Item> Items { get; set; }
    public class Item
    {
        public int Id { get; set; }
        public List<string> skills { get; set; }
    }
    
    public List<string> skills { get; set; }
}