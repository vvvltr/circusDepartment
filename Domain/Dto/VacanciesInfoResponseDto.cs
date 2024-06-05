namespace hh.Domain;

public class VacanciesInfoResponseDto
{    
    public string Name { get; set; }
    public string Description { get; set; }
    public List<string> Skills { get; set; }
    
    public List<KeySkill> KeySkills { get; set; }
    public Address Address { get; set; }
    public Area Area { get; set; }
    public Employment Employment { get; set; }
    public Experience Experience { get; set; }
    public int Id { get; set; }
    public Salary Salary { get; set; }
    public Schedule Schedule { get; set; }
}

public class KeySkill
{
    public string Name { get; set; }
}

public class Area
{
    public string Id { get; set; }
    public string Name { get; set; }
    public string Url { get; set; }
}

public class Salary
{
    public int? From { get; set; }
    public int? To { get; set; }
    public string Currency { get; set; }
}

public class Address
{
    public string City { get; set; }
    public string Street { get; set; }
    public string Building { get; set; }
    public object Description { get; set; }
    public string Raw { get; set; }
    public object Metro { get; set; }
    public List<object> MetroStations { get; set; }
    public string Id { get; set; }
}

public class Schedule
{
    public string Id { get; set; }
    public string Name { get; set; }
}

public class Experience
{
    public string Id { get; set; }
    public string Name { get; set; }
}

public class Employment
{
    public string Id { get; set; }
    public string Name { get; set; }
}