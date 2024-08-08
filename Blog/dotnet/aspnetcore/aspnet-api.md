---
title: ASP.NET Web API
tags: ['C#']
---



# Routing

```cs
app.UseRouter(builder =>
{
  builder.MapRoute(string.Empty, context => 
  {
    return context.Response.WriteAsync($"Welcome to the default route!");
  });
  
  builder.MapGet("foo/{name}/{surname?}", (request, response, routeData) =>
  {
    return response.WriteAsync($"Welcome to Foo, {routeData.Values["name"]} {routeData.Values["surname"]}")''
  });

  builder.MapPost("bar/{number:int}", (request, response, routeData) =>
  {
    return response.WriteAsync($"Welcome to Bar, number is {routeData.Values["number"]}");
  });  
});

// Generating a URI
builder.MapRoute(string.Empty, context =>
{
  var routeValues = new RouteValueDictionary
  {
    { "number", 456 }
  };
  var vpc = new VirtualPathContext(context, null, routeValues, "bar/{number:int}");
  var route = builder.Routes.Single(r => r.ToString().Equals(vpc.RouteName));
  var barUrl = route.GetVirtualPath(vpc).VirtualPath;
  return context.Response.WriteAsync($"URL: {barUrl}");
});
```

```cs
[Route("set-speed/{speed=20:int}", Name = "set_speed")]
```

```cs
[ApiController]
public class ProductsController : BaseController
{
  [HttpGet]
  [Route("/api/products")]
  public IActionResult Get()
  {

  }

  [HttpGet]
  [Route("/api/products/{id}=1:int")]
  public IActionResult Get(int id)
  {

  }
  // Post, Put, Delete
}
```

```cs
[Route("api")] // [Route("api/[controller]")]  --Token Replacement
public class BaseController : Controller {}

[ApiController]
[Route("products")]
public class ProductsController : BaseController
{

  [HttpGet]
  [Route("/abc/products")] // abc/products
  public IActionResult Get()
  {

  } 

  [HttpGet("{id}")] // api/products/{id}
  public IActionResult Get(int id)
  {

  }
}
```

# Model Binding

## Complex Type Model Binding

```cs
public class Product
{
  [FromRoute(Name = "pid")]
  public int Id { get; set; }
  [FromQuery(Name = "name")]
  public string Name { get; set; }
}

public IActionResult Get([FromQuery]Product product) {}
```

## Custom Model Binder

```cs

```

# Model Validation

## Custom Validation

```cs
public class Product_EnsurePrice : ValidationAttribute 
{
  protected override ValidationResult IsValid(object value, ValidationContext validationContext)
  {
    var product = validationContext.ObjectInstance as Product;
    if (product != null && product.Price.HasValue)
    {
      if (!product.Discount.HasValue)
      {
        return new ValidationResult("Product must have a discount if it has a price");
      }
    }
    return ValidationResult.Success;
  }
}
```

## Model Validation with Action Filter

# Versioning

Adding new constraints to Model Classes breaks the old version -> Allpy Action Filters for  Version 2 Endpoints in Api Layer
**Microsoft.AspNetCore.Mvc.Versioning**


## Versioning with Http Header
```cs
builder.Services.AddApiVersioning(options => 
{
  options.ReportApiVersions = true; // the reponse header will include the info about endpoint 
  options.AssumeDefaultVersionWhenUnspecified = true;
  options.DefaultApiVersion = new ApiVersion(1, 0);
  options.ApiVersionReader = new HeaderApiVersionReader("X-API-Version");
});
```
```cs
[ApiVersion("2.0")]
[ApiController]
[Route("api/products")]
public class ProductsV2Controller : BaseController
{
}
```
## Versioning with Route
```cs
```cs
[ApiVersion("2.0")]
[ApiController]
[Route("api/v{v:apiVersion}/products")]
public class ProductsV2Controller : BaseController
{
}
```
## Versioning with Query String
```html
https://localhost:port/api/products?api-version=2.0
```

# Authentication

## API Key

```cs
public class APIKeyAuthFilter : Attribute, IAuthorizationFilter
{
  private const string ApiKeyHeader = "ApiKey";
  private const string ClientIdHeader = "ClientId";

  public void OnAuthorization(AuthorizationFilterContext context)
  {
    if (!context.HttpContext.Request.Headers.TryGetValue(ClientIdHeader, out var clientId))
    {
      context.Result = new UnauthorizedResult();
      return;
    }
    if (!context.HttpContext.Request.Headers.TryGetValue(ApiKeyHeader, out var clientApiKey))
    {
      context.Result = new UnauthorizedResult();
      return;
    }

    // Dependency Injection
    var config = context.HttpContext.RequestServices.GetService(typeof(IConfiguration)) as IConfiguration;
    var apiKey = config.GetValue<string>($"ApiKey:{clientId}");

    if (apiKey != clientApiKey)
    {
      context.Result = new UnauthorizedResult();
    }

  }
}
```

## Token-based Authentication


## JWT Authentication


```cs
var secretKey = builder.Configuration.GetValue<string>("SecretKey") ?? "";
builder.Services.AddAuthentication(options =>
{
  options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
  options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
  options.DefaultSignInScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(options =>
{
  options.SaveToken = true;
  options.TokenValidationParameters = new TokenValidationParameters()
  {
    RequireExpirationTime = true,
    ValidateIssuerSigningKey = true,
    IssuerSigningKey = new SymmetricSecurityKey(Encoding.ASCII.GetBytes(secretKey)),
    ValidateLifetime = true,
    ValidateAudience = true,
    ValidateIssuer = true,
    ValidIssuer = builder.Configuration.GetValue<string>("AuthToken:Issuer"),
    ValidAudience = builder.Configuration.GetValue<string>("AuthToken:Audience"),
    ClockSkew = TimeSpan.Zero
  };

  // The orders that the events are triggered:
  // OnMessageReceived -> OnTokenValidated (if there is Authorize Attribute)
  // -> OnChallenge
  // OnAuthenticationFailed is triggered when an error occurs during validation
  options.Events = new JwtBearerEvents()
  {
    OnTokenValidated = context => 
    {
      var tokenManager = context.HttpContext.RequestServices.GetRequiredService<ITokenManager>();
      return Task.Run(() => tokenManager.ValidateAccessToken(context));
    },
    OnAuthenticationFailed = context => 
    {
      return Task.CompletedTask;
    },
    OnMessageReceived = context => 
    {
      return Task.CompletedTask;
    },
    OnChallenge = context => 
    {
      return Task.CompletedTask;
    }
  };
});
```

```cs
[HttpPost]
[Route("/authenticate")]
public IActionResult Authenticate([FromBody] Credential credential)
{
  if (ModelState.IsValid)
  {
    // Verify the credential
    if (credential.UserName == "admin" && credential.Password == "123")
    {
      (string accessToken, DateTime accessTokenExpiresAt) = (new TokenManager()).CreateAccessToken(user);
      (string refreshToken, DateTime refreshTokenExpiresAt) = (new TokenManager()).CreateRefreshToken(user);
      
      // Save tokens and expiry dates to Database

      return Ok(new
      {
        access_token = accessToken,
        refresh_token = refreshToken,
        access_expires_at = accessTokenExpiresAt,
        refresh_expires_at = refreshTokenExpiresAt
      });
    }              
  }
  return Unauthorized();
}

[HttpPost]
[Route("/refresh-token")]
public IActionResult RefreshToken([FromBody] string refreshToken)
{
  return Ok(new TokenManager().ValidateRefreshToken(refreshToken));
}
```



```cs
public class TokenManager : ITokenManager
{
  private readonly IConfiguration configuration;

  public TokenManager(IConfiguration configuration)
  {
    this.configuration = configuration;
  }
  public (string, DateTime) CreateAccessToken(User user)
  {
    // Creating the security context
    var issuer = configuration.GetValue<string>("AuthToken:Issuer");
    var audience = configuration.GetValue<string>("AuthToken:Audience");
    var secretKey = Encoding.ASCII.GetBytes(configuration.GetValue<string>("AuthToken:SecretKey") ?? "");
    var expiresAt = DateTime.UtcNow.AddMinutes(20);
    var claims = new List<Claim>()
    {
      new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString(), ClaimValueTypes.String, issuer),
      new Claim(JwtRegisteredClaimNames.Iss, issuer, ClaimValueTypes.String),
      new Claim(JwtRegisteredClaimNames.Iat, DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString(), ClaimValueTypes.Integer64),
      new Claim(JwtRegisteredClaimNames.Exp, new DateTimeOffset(expiresAt).ToUnixTimeSeconds().ToString(), ClaimValueTypes.Integer64),
      new Claim(ClaimTypes.Name, user.FullName),
      new Claim(ClaimTypes.Email, user.Email),
    };
    // generate the JWT
    // install package System.IdentityModel.Tokens.Jwt 
    // and Microsoft.AspNetCore.Authentication.JwtBearer 

    var jwtSecurityToken = new JwtSecurityToken(
      issuer: issuer,
      audience: audience,
      claims: claims,
      notBefore: DateTime.UtcNow,
      expires: expiresAt,
      signingCredentials: new SigningCredentials(
        new SymmetricSecurityKey(secretKey),
        SecurityAlgorithms.HmacSha256Signature
      )
    );
    return (new JwtSecurityTokenHandler().WriteToken(jwtSecurityToken), expiresAt);
  }

  public (string, DateTime) CreateRefreshToken(User user)
  {
    // Creating the security context
    var issuer = configuration.GetValue<string>("AuthToken:Issuer");
    var audience = configuration.GetValue<string>("AuthToken:Audience");
    var secretKey = Encoding.ASCII.GetBytes(configuration.GetValue<string>("AuthToken:SecretKey") ?? "");
    var expiresAt = DateTime.UtcNow.AddHours(4);
    var refreshClaims = new List<Claim>()
    {
      new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString(), ClaimValueTypes.String, issuer),
      new Claim(JwtRegisteredClaimNames.Iss, issuer, ClaimValueTypes.String),
      new Claim(JwtRegisteredClaimNames.Iat, DateTimeOffset.UtcNow.ToUnixTimeSeconds().ToString(), ClaimValueTypes.Integer64),
      new Claim(JwtRegisteredClaimNames.Exp, new DateTimeOffset(expiresAt).ToUnixTimeSeconds().ToString(), ClaimValueTypes.Integer64),
      new Claim(ClaimTypes.SerialNumber, Guid.NewGuid().ToString(), ClaimValueTypes.String, issuer),
    };
    // generate the JWT
    // install package System.IdentityModel.Tokens.Jwt 
    // and Microsoft.AspNetCore.Authentication.JwtBearer 

    var jwtSecurityToken = new JwtSecurityToken(
      issuer: issuer,
      audience: audience,
      claims: claims,
      notBefore: DateTime.UtcNow,
      expires: expiresAt,
      signingCredentials: new SigningCredentials(
        new SymmetricSecurityKey(secretKey),
        SecurityAlgorithms.HmacSha256Signature
      )
    );
    return (new JwtSecurityTokenHandler().WriteToken(jwtSecurityToken), expiresAt);
  }

  public async Task ValidateAccessToken(TokenValidatedContext context)
  {
    var claims = context.Principal.Claims.ToList();

    if (claims.Count == 0)
    {
      context.Fail("This token has no claim");
      return;
    }

    var identity = context.Principal.Identity as ClaimsIdentity;
    if (identity.FindFirst(ClaimTypes.Email) != null)
    {
      var userName = identity.FindFirst(ClaimTypes.Email).Value;
      // Find the user
      // var user = _userService.FindByEmail(userName);
      if (user == null)
      {
        context.Fail("Invalid token");
        return;
      }
    }

    if (identity.FindFirst(JwtRegisteredClaimNames.Exp) == null)
    {
      var expiryDate = identity.FindFirst(JwtRegisteredClaimNames.Exp).Value;
      var date = DateTimeOffset.FromUnixTimeSeconds(long.Parse(expiryDate)).DateTime;
      var minutes = date.Subtract(DateTime.UtcNow).TotalMinutes;
      if (minutes < 0)
      {
        context.Fail("The token is expired");
        return;
      }
    }
  }

  public Token ValidateRefreshToken(string refreshToken)
  {
    
    var secretKey = Encoding.ASCII.GetBytes(configuration.GetValue<string>("SecretKey") ?? "");
    var claimPrincipal = new JwtSecurityTokenHandler().ValidateToken(
      refreshToken,
      new TokenValidationParameters()
      {
        RequireExpirationTime = true,
        ValidateIssuerSigningKey = true,
        IssuerSigningKey = new SymmetricSecurityKey(secretKey),
        ValidateAudience = true,
        ValidateIssuer = true,
        ValidateLifetime = true,
        ValidIssuer = configuration.GetValue<string>("AuthToken:Issuer"),
        ValidAudience = configuration.GetValue<string>("AuthToken:Audience"),
        ClockSkew = TimeSpan.Zero
      }, out _
    );

    if (claimPrincipal == null) return;
    // if use Serial Number to check the Refresh Token
    string serialNumber = claimPrincipal.Claims.FirstOrDefault(c => c.Type == ClaimTypes.SerialNumber)?.Value;
    if (!string.IsNullOrEmpty(serialNumber))
    {
      // Check whether the Refresh Token exists in database or not
      // The Check method will return the User possessing this Token
      // If it exists
      (string newAccessToken, DateTime accessTokenExpiresAt) = CreateAccessToken(user);
      (string newRefreshToken, DateTime refreshTokenExpiresAt) = CreateRefreshToken(user);

      return new Token
      {
        AccessToken = newAccessToken,
        RefreshToken = newRefreshToken,
        AccessTokenExpiryDate = accessTokenExpiresAt,
        RefreshTokenExpiryDate = refreshTokenExpiresAt
      };
    }
  }
}
```

### Authorization

```cs
[Authorize(policy:"AdminOnly")]
```
```cs
builder.Services.AddAuthorization(options =>
{
  options.AddPolicy("AdminOnly", policy => policy.RequireClaim("Admin"));
});
```

### Store and Reuse Token in Session

```cs
builder.Services.AddSession(options =>
{
  options.Cookie.HttpOnly = true;
  options.IdleTimeout = TimeSpan.FromMinutes(30);
  options.Cookie.IsEssential = true;
});
```

# Caching

## Memory Cache

## Distributed Cache

**Install package NCache.Microsoft.Extensions.Caching**

```cs
builder.Services.AddCacheDistributedCache(config => 
{
  config.CacheName = "";
  config.EnableLogs = true;
  config.ExceptionsEnabled = true;
});
```

```cs
public class DistributedCacheService
{
  private readonly IDistributedCache cache;

  public DistributedCacheService(IDistributedCache cache)
  {
    this.cache = cache;
  }
  public async Task Set<T>(string key, T value)
  {
    await cache.SetStringAsync(key,JsonConvert.SerializeObject(value), new DistributedCacheEntryOptions
    {
      AbsoluteExpirationRelativeToNow = TimeSpan.FromDays(1),
      SlidingExpiration = TimeSpan.FromMinutes(30),
    });
  }

  public async Task<T> Get<T>(string key)
  {
    string data = await cache.GetStringAsync(key);
    if (!string.IsNullOrEmpty(data))
    {
      return JsonConvert.DeserializeObject<T>(data);
    }
    return default;
  }

  public async Task Remove(string key)
  {
    await cache.RemoveAsync(key);
  }
}
```

## Redis

## Sql Cache


# Open API

```cs
builder.Services.AddVersionedApiExplorer(options => options.GroupNameFormat = "'v'VVV");
builder.Services.AddSwaggerGen();

if (app.Environment.IsDevelopment())
{
  app.UseSwagger();
  app.UseSwaggerUI(options =>
  {
    options.SwaggerEndpoint("/swagger/v1/swagger.json", "WebApi v1");
  });
}
```
