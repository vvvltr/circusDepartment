using System.Security.Cryptography;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;

namespace hh.Helper;

public static class PasswordHasher
{
    public static byte[] GenerateSalt()
    {
        byte[] salt = new byte[128 / 8];
        using (var rng = RandomNumberGenerator.Create())
        {
            rng.GetBytes(salt);
        }
        return salt;
    }

    public static string HashPassword(string password, byte[] salt)
    {
        string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
            password: password,
            salt: salt,
            prf: KeyDerivationPrf.HMACSHA256,
            iterationCount: 10000,
            numBytesRequested: 256 / 8));
        return hashed;
    }

    public static bool VerifyPassword(string hashedPassword, string password, byte[] salt)
    {
        string hashedInputPassword = HashPassword(password, salt);
        return hashedPassword == hashedInputPassword;
    }
}