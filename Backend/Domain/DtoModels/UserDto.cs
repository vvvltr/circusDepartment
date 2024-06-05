using System.ComponentModel.DataAnnotations;

namespace hh.Domain;

public class UserDto
{
    [Required]
    [EmailAddress]
    public string Email { get; set; }

    [Required]
    [DataType(DataType.Password)]
    public string Password { get; set; }

}