# Serverske veb tehnologije - Vežbe 6

## Spring Security

- Radni okvir za podršku bezbednosti u Spring aplikacijama
- Zasniva se na tehnikama deklarativnog programiranja
- Pokriva autentifikaciju i autorizaciju na nivou HTTP zahteva, ali i poziva
  pojedinačnih metoda

### Filtriranje veb zahteva

- Spring sadrži generički filter `DelegatingFilterProxy` koji može da
  delegira posao specifičnom filteru
- Spring Security tu može da injektuje svoj specifičan filter
  `springSecurityFilterChain`
- Spring Boot automatski injektuje ovaj bean kroz anotaciju
  `@EnableWebSecurity`

### Konfiguracija bezbednosti

- U klasi koja nasleđuje `WebSecurityConfigurerAdapter` klasu
- Redefinišu se sledeće metode:
  - `configureAuthentication (AuthenticationManagerBuilder )`
    - definiše način utvrđivanja identiteta korisnika pri autentikaciji
  - `configure (HttpSecurity )`
    - definiše prava pristupa za zahteve ka određenim URL
  - `configure (WebSecurity )`
    - generalna bezbednost veb aplikacije
    - ignorisanje resursa, firewall, ...

### Autentifikacija korisnika

- Za autentikaciju korisnika potrebno je da postoje uskladišteni podaci o
  korisnicima aplikacije i njihovim ulogama u sistemu
- Spring putem JDBC API-ija ima ugrađenu podršku za različite standardne pristupe skladištenju
  - Može se realizovati i nestandardna podrška za utvrđivanje identiteta
    korisnika

- Korisnik je predstavljen interfejsom `UserDetails`
- Za korisnika se minimalno evidentira:
  - korisničko ime
  - lozinka
  - lista uloga (uloga predstavljena interfejsom `GrantedAuthority`)
- Način pristupa je predstavljen interfejsom `UserDetailsService`
- Interfejs UserDetailsService ima samo metodu `loadUserByUsername(String username)`
- Potrebno je implementirati interfejs i redefinisati ovu metodu na
  proizvoljan način npr. korišćenjem JPA repozitorijuma za pristup bazi korisnika
- Pri konfiguraciji autentifikacije u metodi `configure` je potrebno navesti
  koja klasa je zadužena za autentifikaciju

### Presretanje HTTP zahteva

- Spring security presreće HTTP zahteve posebnim filterom
- Može se definisati da li korisnik ima pravo da dobije odgovor na zahtev koji je
  poslat na određenu putanju
  - Definisanje se vrši u metodi `configure(HttpSecurity )`
- Definiše se ko ima pravo da pristupi putanji:
  - ulogovani korisnici
  - svi korisnici
  - oni koji imaju određenu ulogu u sistemu
  - ...

### Skladištenje lozinki

- **Lozinke se nikad ne čuvaju u bazi podataka kao plain text**
- Spring security ima ugrađenu podršku za skladištenje lozinki u šifrovanom
  obliku
- Dovoljno je instancirati odgovarajuću klasu za šifrovanje lozinki i primeniti je
  pri konfigurisanju načina identifikacije korisnika

```java
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
```

### Autentifikacija bazirana na tokenima

- HTTP protokol je stateless pa je potreban mehanizam identifikacije klijenta koji šalje višestruke zahteve
- Nakon uspešne autentifikacije, klijent dobija token koji je server generisao
- Sa svakim sledećim zahtevom, klijent šalje token kao informaciju o svom identitetu

- JSON veb token (JWT) https://jwt.io/
  - Trenutno dominantan format u kojem se tokeni za autentikaciju
    reprezentuju
  - Predstavlja string koji se sastoji iz tri dela:
    - zaglavlje (_header_)
      - tip tokena i algoritam kriptovanja
    - sadržaj (_payload_)
      - podaci o korisniku na kojeg se token odnosi, rok trajanja tokena
    - potpis (_signature_)
      - string generisan šifrovanjem zaglavlja, sadržaja i serverove tajne reči

- Realizacija JWT autentikacije kroz Spring Security:
  - Potrebno je nakon uspešne prijave na sistem (login) generisati JWT token i poslati
    ga klijentu kao objekat HTTP odgovora.
  - Svaki naredni zahtev klijenta ka serveru je potrebno presresti kako bi se proverilo da li sadrži
    validan JWT token
    - presretanje se vrši dodavanjem novog filtera
    - filter će postaviti odgovarajuću autentifikaciju ako je token validan
    - dalje Spring security dozvoljava pristup putanji zavisno od uspešnosti
      autentikfiacije i ranije konfigurisanih prava pristupa putanji