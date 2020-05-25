# movie-api
A CRUD rest Api 

To disable spring security
@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class,
    ManagementWebSecurityAutoConfiguration.class}
)

comment AuthenticationController 
comment SecurityConfig