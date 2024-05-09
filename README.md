# ButorWebAruhaz- Kozocsay Lajos Gergő

## Android SDK: 30 (Ennél újabb sdk-val kifagy az alkalmazás ha galériából szeretne az ember új termék képet feltölteni)
## CRUD részek-hez admin login: 
### email: admin@ad.min
### jelszó: admin123

## CheatSheet a pontozó táblázathoz


| Elem                                                                       | Kódrész(Where it is)                                                                                                                                                                       |
| -------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Fordítási hiba                                                             | -                                                                                                                                                                                          |
| Futattási hiba                                                             | -                                                                                                                                                                                          |
| Firebase autentikáció                                                      | Login -> MainActivity:67-78, Regisztráció -> RegisterActivity:78-94                                                                                                                        |
| Adatmodell definiálása                                                     | Model/ItemModel:*                                                                                                                                                                          |
| Legalább 3 különböző activity vagy fragmens használata                     | MainActivity, RegisterActivity, HomeActivity, AddItemActivity, ItemUpdateAndDeleteActivity, CartActivity                                                                                   |
| Beviteli mezők megfelelő típusa                                            | res/layout/activity_main.xml: 32;46, res/layout/activity_register.xml: 33;47;61, res/layout/activity_cart.xml: 63                                                                          |
| ConstraintLayout és még egy másik layout típus használata                  | ConstraintLayout Példa: res/layout/activity_main RelativeLayout Példa: res/layout/activity_update_and_delete                                                                               |
| Reszponzív                                                                 | -(Hellyel-közel, A listában lévő legutolsó elem leírása nem látszódik, illetve új termék hozzáadása során az EditText eltűnik a billentyűzet alá)                                          |
| Legalább 2 különböző animáció                                              | -                                                                                                                                                                                          |
| Intentek használata: navigáció megvalósítva                                | Példa: ItemUpdateAndDeleteActivity: 75-114                                                                                                                                                 |
| Legalább egy lifeCycle hook                                                | MainActivity: 82(onStart)                                                                                                                                                                  |
| Legalább egy olyan erőforrás amelyhez kell permission                      | AndroidManifest.xml: 5-17(3 darab)                                                                                                                                                         |
| Legalább egy notification vagy alarm manager vagy job scheduler használata | -                                                                                                                                                                                          |
| CRUD műveletek                                                             | (Nincsenenk külön szálon) Create -> AddItemActivity: 98-139; Read -> HomeActivity: 91-115; Update -> ItemUpdateAndDeleteActivity: 116-126; Delete -> ItemUpdateAndDeleteActivity: 128- 138 |
| Legalább 2 komplex lekérdezés                                              | (Ha ez annak számít) CartActivity: 109-129                                                                                                                                                 |

