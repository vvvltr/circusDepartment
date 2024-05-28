using System.ComponentModel.DataAnnotations;

namespace hh.Domain;

public class SignUpModel
{
    [Required]
    [EmailAddress]
    public string Username { get; set; }

    [Required]
    [DataType(DataType.Password)]
    public string Password { get; set; }

}