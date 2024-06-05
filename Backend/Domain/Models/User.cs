using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;

namespace hh.Domain.Models;

public class User
{
    [Key]
    public int Id { get; set; }
    
    [Required]
    [EmailAddress]
    public string Email { get; set; }
    
    [Required]
    public string Password { get; set; }
    
    [Required]
    public byte[] Salt { get; set; }
    
    /// <summary>
    /// Список навыков и компетенций
    /// </summary>
    public string? Competencies { get; set; }
}