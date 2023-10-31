## Classi dei Requisiti
### Requisiti Durevoli e Volatili
#### Durevoli:
Comprendono quei requisiti che rimarranno fissi durante il progetto. Nel caso della nostra applicazione, questi includono la registrazione degli utenti, la gestione delle date di scadenza degli alimenti e la notifica agli utenti.

#### Volatili:
Sono l’area di requisiti che potrebbero evolvere con il tempo o con lo sviluppo. Nel nostro progetto, questi potrebbero includere funzionalità come la condivisione delle liste di alimenti tra gli utenti, l'implementazione di suggerimenti per il consumo, e le statistiche di utilizzo.

### Requisiti Funzionali e Non Funzionali
#### Requisiti Funzionali
**ID: RF-01**  
**Caso d'uso: Registrazione Utente**  
**Definizione:**  L'applicazione deve permettere all'utente di registrarsi fornendo alcuni dati personali.  
**Motivazione:** Per utilizzare tutte le funzionalità dell'app, l'utente deve essere registrato.  
**Influisce:**  Tutte le altre funzionalità dell'app.  
**Priorità:**  Alta

**ID: RF-02**  
**Caso d'uso: Aggiungere Alimento**  
**Definizione:**  L'utente deve essere in grado di aggiungere un alimento specificando nome, data di scadenza e, se desiderato, una foto.  
**Motivazione:** Per monitorare le date di scadenza degli alimenti.  
**Influisce:**  Notifiche, Visualizzazione elenco alimenti.  
**Priorità:**  Alta

**ID: RF-03**  
**Caso d'uso: Visualizzare Elenco Alimenti**  
**Definizione:**  L'utente deve poter visualizzare un elenco degli alimenti inseriti, ordinati in base alla data di scadenza.  
**Motivazione:** Per tenere traccia degli alimenti e delle loro date di scadenza.  
**Influisce:**  Notifiche.  
**Priorità:**  Media

**ID: RF-04**  
**Caso d'uso: Ricevere Notifiche**  
**Definizione:**  L'applicazione deve inviare notifiche all'utente quando un alimento sta per scadere.  
**Motivazione:** Per ridurre lo spreco di cibo avvisando l'utente in tempo.  
**Influisce:**  Visualizzazione elenco alimenti.  
**Priorità:**  Alta

**ID: RF-05**  
**Caso d'uso: Modificare o Eliminare Alimenti**  
**Definizione:**  L'utente deve essere in grado di modificare o eliminare un alimento inserito.  
**Motivazione:** Per aggiornare o rimuovere informazioni obsolete.  
**Influisce:**  Visualizzazione elenco alimenti, Notifiche.  
**Priorità:**  Media

**ID: RF-06**  
**Caso d'uso: Contrassegnare Alimento come Consumato**  
**Definizione:**  L'utente deve poter contrassegnare un alimento come consumato, rimuovendolo dall'elenco delle notifiche.  
**Motivazione:** Per evitare notifiche non necessarie.  
**Influisce:**  Notifiche.  
**Priorità:**  Media

**ID: RF-07**  
**Caso d'uso: Condividere Lista Alimenti**  
**Definizione:**  L'utente deve essere in grado di condividere la sua lista di alimenti con altri utenti.  
**Motivazione:** Per facilitare la gestione condivisa di una dispensa o frigorifero in ambienti condivisi.  
**Influisce:**  Registrazione Utente.  
**Priorità:**  Bassa

**ID: RF-08**  
**Caso d'uso: Gestire Impostazioni Account**  
**Definizione:**  L'utente deve poter gestire le impostazioni del suo account, come la frequenza delle notifiche.  
**Motivazione:** Per personalizzare l'esperienza dell'utente.  
**Influisce:**  Notifiche.  
**Priorità:**  Bassa
