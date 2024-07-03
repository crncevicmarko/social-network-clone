package rs.ac.uns.ftn.svtvezbe06.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import rs.ac.uns.ftn.svtvezbe06.service.implementation.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
        // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
        authProvider.setUserDetailsService(userDetailsService());
        // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
        // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // svim korisnicima dopusti da pristupe sledecim putanjama:
        // komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
        // ovo znaci da server ne pamti nikakvo stanje, tokeni se ne cuvaju na serveru
        // ovo nije slucaj kao sa sesijama koje se cuvaju na serverskoj strani - STATEFUL aplikacija
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll()	// /h2-console/** ako se koristi H2 baza)
                .antMatchers(HttpMethod.POST, "/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/users/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/users/user/{userName}").permitAll()
        		.antMatchers(HttpMethod.PATCH, "/users/user/{id}").permitAll()
        		.antMatchers(HttpMethod.POST, "/users/block/{id}").permitAll()
        		.antMatchers(HttpMethod.POST, "/users/unblock/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/users/search").permitAll()
                .antMatchers(HttpMethod.GET, "/users/getAllByUserId").permitAll()
                .antMatchers(HttpMethod.GET, "/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/posts/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/{groupId}").permitAll()
                .antMatchers(HttpMethod.PUT, "/posts/update/{id}").permitAll() //mora PUT
                .antMatchers(HttpMethod.DELETE, "/posts/delete/{id}").permitAll()
        		.antMatchers(HttpMethod.GET, "/posts/date/{date}").permitAll()
        		.antMatchers(HttpMethod.GET, "/posts/numberOfLikes/{numberOfLikes}").permitAll()
                .antMatchers(HttpMethod.GET, "/groups").permitAll()
                .antMatchers(HttpMethod.POST, "/groups").permitAll()
                .antMatchers(HttpMethod.GET, "/groups/{id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/groups/update/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/groups/delete/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/groupRequests").permitAll()
                .antMatchers(HttpMethod.POST, "/groupRequests/save/{groupid}").permitAll()
                .antMatchers(HttpMethod.PUT, "/groupRequests/update/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/groupRequests/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/friendRequests").permitAll()
                .antMatchers(HttpMethod.GET, "/friendRequests/all").permitAll()
                .antMatchers(HttpMethod.POST, "/friendRequests/{id}").permitAll()
                .antMatchers(HttpMethod.PUT, "/friendRequests/{id}/{approved}").permitAll()
                .antMatchers(HttpMethod.GET, "/comments").permitAll()
                .antMatchers(HttpMethod.POST, "/comments").permitAll()
                .antMatchers(HttpMethod.PATCH, "/comments/update/{id}").permitAll()
                .antMatchers(HttpMethod.DELETE, "/comments/delete/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/comments/sort/{turn}/{datumOd}/{datumDo}").permitAll()
                .antMatchers(HttpMethod.PUT, "/reactions/{commentId}/{postId}").permitAll()
                .antMatchers(HttpMethod.GET, "/reactions").permitAll()
                .antMatchers(HttpMethod.GET, "/reactions/user/{userId}").permitAll()
                .antMatchers(HttpMethod.GET, "/reactions/post/{postId}").permitAll()
                .antMatchers(HttpMethod.POST, "/reports/{commentId}/{postId}").permitAll()
                .antMatchers(HttpMethod.GET, "/reports").permitAll()                
                .antMatchers(HttpMethod.PUT, "/banns").permitAll()
                .antMatchers(HttpMethod.POST, "/api/index").permitAll()
                .antMatchers(HttpMethod.GET, "/api/file/{filename}").permitAll()
                .antMatchers(HttpMethod.POST, "/api/search/simple").permitAll()
                .antMatchers(HttpMethod.POST, "/api/search/advanced").permitAll()
                .antMatchers(HttpMethod.POST, "/groups/search/simple").permitAll()
                .antMatchers(HttpMethod.POST, "/groups/search/advanced").permitAll()
                .antMatchers(HttpMethod.POST, "/groups/search/byOneChoice").permitAll()
                .antMatchers(HttpMethod.POST, "/groups/search/byNumOfPosts").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/search/simple").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/search/advanced").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/search/byNumOfLikes").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/search/byNumOfComments").permitAll()
                .antMatchers(HttpMethod.POST, "/posts/search/byAverageNumOfLikes").permitAll()
                // .antMatchers(HttpMethod.GET, "/clubs/{id}/**").access("@webSecurity.checkClubId(authentication,request,#id)")
                // ukoliko ne zelimo da koristimo @PreAuthorize anotacije nad metodama kontrolera, moze se iskoristiti hasRole() metoda da se ogranici
                // koji tip korisnika moze da pristupi odgovarajucoj ruti. Npr. ukoliko zelimo da definisemo da ruti 'admin' moze da pristupi
                // samo korisnik koji ima rolu 'ADMIN', navodimo na sledeci nacin:
                //.antMatchers("/api/clubs").hasRole("ADMIN")// ili .antMatchers("/admin").hasAuthority("ROLE_ADMIN")

                // za svaki drugi zahtev korisnik mora biti autentifikovan
                .anyRequest().authenticated().and()
                // za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
                .cors().and()

                // umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT tokena umesto cistih korisnickog imena i lozinke (koje radi BasicAuthenticationFilter)
                .addFilterBefore(new AuthenticationTokenFilter(userDetailsService(), tokenUtils), BasicAuthenticationFilter.class);

        // zbog jednostavnosti primera ne koristimo Anti-CSRF token (https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
        http.csrf().disable();


        // ulancavanje autentifikacije
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    // metoda u kojoj se definisu putanje za igorisanje autentifikacije
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
        // Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u
        // Dozvoljena POST metoda na ruti /api/users/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
        return (web) -> web.ignoring().antMatchers(HttpMethod.POST, "/users/login")

                // Ovim smo dozvolili pristup statickim resursima aplikacije
                .antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
                        "/**/*.html", "/**/*.css", "/**/*.js");

    }
}
