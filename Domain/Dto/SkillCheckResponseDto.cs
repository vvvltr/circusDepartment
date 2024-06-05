namespace hh.Domain;

public class SkillCheckResponseDto
{
    public List<Item> Items { get; set; }
    public class Item
    {
        public int Id { get; set; }
        public string Skills { get; set; }
    }
    
    public List<string> Skills { get; set; }
}