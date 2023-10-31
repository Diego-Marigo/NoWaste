# NODI DEL GRAFO

1. **Registrazione Utente**
    - **Scopo**: Permettere agli utenti di registrarsi nell'app.
    - **Attori**: Utente non registrato.
    - **Pre-condizioni**: L'app è installata.
    - **Trigger**: Utente avvia l'app e sceglie di registrarsi.
    - **Descrizione**: L'utente inserisce dati personali per creare un account.
    - **Alternative**: Nessuna.
    - **Post-condizioni**: Account creato e utente loggato.
    - **Questioni aperte**: Gestione della privacy dei dati?

1. **Aggiungere Alimento**
    - **Scopo**: Inserire nuovi alimenti per tracciare la loro scadenza.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Utente è loggato.
    - **Trigger**: Utente sceglie di aggiungere un alimento.
    - **Descrizione**: L'utente inserisce dettagli dell'alimento.
    - **Alternative**: Aggiungere una foto dell'alimento.
    - **Post-condizioni**: Alimento aggiunto alla lista.
    - **Questioni aperte**: Limiti sul numero di alimenti?

3. **Visualizzare Elenco Alimenti**
    - **Scopo**: Mostrare la lista degli alimenti inseriti.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Almeno un alimento è stato inserito.
    - **Trigger**: Utente apre la lista degli alimenti.
    - **Descrizione**: L'utente può vedere e ordinare la lista.
    - **Alternative**: Visualizzare dettagli aggiuntivi.
    - **Post-condizioni**: Nessuna.
    - **Questioni aperte**: Filtri o ricerca?
    
4. **Modificare Impostazioni**
    - **Scopo**: Personalizzare le impostazioni dell'app.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Utente è loggato.
    - **Trigger**: Utente accede alle impostazioni.
    - **Descrizione**: L'utente può cambiare le notifiche, visualizzazione, ecc.
    - **Alternative**: Nessuna.
    - **Post-condizioni**: Impostazioni aggiornate.
    - **Questioni aperte**: Reset alle impostazioni di default?

5. **Ricevere Notifiche**
    - **Scopo**: Ricevere avvisi sulla scadenza degli alimenti.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Almeno un alimento è stato inserito e le notifiche sono attive.
    - **Trigger**: Data di scadenza si avvicina.
    - **Descrizione**: L'utente riceve una notifica sull'app o via altri metodi d'avviso.
    - **Alternative**: Ricevere notifica via email.
    - **Post-condizioni**: Utente informato.
    - **Questioni aperte**: Quanto tempo prima della scadenza inviare la notifica?
    
6. **Condividere Lista Alimenti**
    - **Scopo**: Condividere informazioni sulla lista degli alimenti con altri utenti.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Almeno un alimento è stato inserito.
    - **Trigger**: Utente sceglie di condividere la lista.
    - **Descrizione**: Invia la lista via un link o QR code.
    - **Alternative**: Condivisione via email o app di messaggistica.
    - **Post-condizioni**: Lista condivisa.
    - **Questioni aperte**: Gestione dei permessi?
    
7. **Eliminare o Modificare Alimento**
    - **Scopo**: Rimuovere o aggiornare i dettagli di un alimento esistente.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Almeno un alimento è stato inserito.
    - **Trigger**: Utente sceglie un alimento dalla lista.
    - **Descrizione**: L'utente può modificare i dettagli o eliminare l'alimento.
    - **Alternative**: Contrassegnare come consumato.
    - **Post-condizioni**: Lista aggiornata.
    - **Questioni aperte**: Conferma prima dell'eliminazione?
    
8. **Gestire Account**
    - **Scopo**: Gestire le informazioni dell'account dell'utente.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Utente è loggato.
    - **Trigger**: Utente accede alle impostazioni dell'account.
    - **Descrizione**: Modifica email, password, ecc.
    - **Alternative**: Eliminazione dell'account.
    - **Post-condizioni**: Account aggiornato.
    - **Questioni aperte**: Verificare le modifiche via email?

### Extra

9. **Statistiche di Utilizzo**
    - **Scopo**: Fornire statistiche sull'uso dell'app.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Utente è loggato.
    - **Trigger**: Utente accede alla sezione delle statistiche.
    - **Descrizione**: Visualizza dati come il numero di alimenti salvati, scaduti, ecc.
    - **Alternative**: Nessuna.
    - **Post-condizioni**: Nessuna.
    - **Questioni aperte**: Che tipo di statistiche mostrare?
   
10. **Impostazioni di Backup**
    - **Scopo**: Salvare e ripristinare i dati dell'app.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Utente è loggato.
    - **Trigger**: Utente accede alle impostazioni di backup.
    - **Descrizione**: L'utente può eseguire un backup o ripristinare i dati.
    - **Alternative**: Backup su cloud.
    - **Post-condizioni**: Dati salvati o ripristinati.
    - **Questioni aperte**: Cifratura dei dati di backup?

11. **Ricerca Alimenti**
    - **Scopo**: Trovare un alimento specifico nella lista.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Almeno un alimento è stato inserito.
    - **Trigger**: Utente inizia a digitare nella barra di ricerca.
    - **Descrizione**: La lista si aggiorna per mostrare solo gli alimenti corrispondenti.
    - **Alternative**: Nessuna.
    - **Post-condizioni**: Nessuna.
    - **Questioni aperte**: Ricerca avanzata?
    
12. **Suggerimenti per il Consumo**
    - **Scopo**: Suggerire ricette o modi per consumare gli alimenti in scadenza.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Almeno un alimento è vicino alla data di scadenza.
    - **Trigger**: Notifica di alimento in scadenza.
    - **Descrizione**: L'app suggerisce modi per utilizzare l'alimento.
    - **Alternative**: Link a ricette online.
    - **Post-condizioni**: Nessuna.
    - **Questioni aperte**: Quali fonti usare per i suggerimenti?

13. **Feedback e Supporto**
    - **Scopo**: Fornire un meccanismo per ricevere feedback o richiedere supporto.
    - **Attori**: Utente registrato.
    - **Pre-condizioni**: Utente è loggato.
    - **Trigger**: Utente accede alla sezione di feedback.
    - **Descrizione**: L'utente può inviare feedback o richiedere supporto.
    - **Alternative**: Segnalazione di bug.
    - **Post-condizioni**: Feedback inviato.
    - **Questioni aperte**: Come gestire le richieste di supporto?


# ARCHI DEL GRAFO
#### Principali
**Utente non registrato** → Registrazione Utente → Utente registrato
2. **Utente registrato** 
→ Aggiungere Alimento
→ Visualizzare Elenco Alimenti
→ Ricevere Notifiche
→ Modificare Impostazioni
→ Condividere Lista Alimenti
→ Eliminare o Modificare Alimento
→ Gestire Account
→ Statistiche di Utilizzo
→ Impostazioni di Backup
→ Ricerca Alimenti
→ Suggerimenti per il Consumo
→ Feedback e Supporto
#### Archi tra varie azioni
- Visualizzare Elenco Alimenti → Eliminare o Modificare Alimento → Ricevere Notifiche
- Visualizzare Elenco Alimenti → Ricerca Alimenti (La funzione di ricerca sarebbe utilizzata all'interno della visualizzazione della lista)
- Modificare Impostazioni → Condividere Lista Alimenti (Le impostazioni potrebbero includere preferenze sulla condivisione)
- Aggiungere Alimento → Suggerimenti per il Consumo (Aggiungendo un alimento, si potrebbero generare suggerimenti su come consumarlo. Richiede però di avere un sistema di generazione suggerimenti)
- Gestire Account → Impostazioni di Backup (La gestione dell'account potrebbe includere opzioni per il backup dei dati)
- Ricevere Notifiche → Suggerimenti per il Consumo (Una notifica di scadenza potrebbe innescare un suggerimento su come utilizzare l'alimento)
- Statistiche di Utilizzo → Feedback e Supporto (Dopo aver visualizzato le statistiche, un utente potrebbe voler fornire feedback)

###### Non ben giustificati
- Condividere Lista Alimenti → Visualizzare Elenco Alimenti (La lista da condividere è la lista visualizzata)
- Aggiungere Alimento → Ricevere Notifiche (notifica opzionale all'aggiunta di un alimento? Magari se il profilo è collegato a più coinquilini)
- Modificare Impostazioni → Ricevere Notifiche (per le impostazioni condivise? Magari possiamo avere una configurazione per l'app, ed una configurazione per una lista. Nel caso di una lista condivisa, potrebbe essere avvisare tutti)
