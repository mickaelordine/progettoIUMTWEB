<template>
    <div class="menu">
        <button v-on:click="SeeRepetitions" class="button-5">Vedi ripetizioni passate</button>
        <button  v-on:click="LogOut" class="button-6">LogOut</button>
    </div>
    <br/>
    <br/>
    <br/><br/>
    <br/>
    <br/>
    <h3>Scegli la materia di cui vuoi ripetizioni</h3>
    <div class="table_rep">
          <button class="button-78" v-on:click="goToTutti">Tutte</button>
          <br/>
          <p>oppure cerca per materia specifica</p>
          <button class="button-77" v-on:click="goToIUM">IUM</button>
          <button class="button-77" v-on:click="goToProg3">Prog 3</button>
          <button class="button-77" v-on:click="goToProg2">prog 2</button> 
          <br/>
          <button class="button-77" v-on:click="goToReti">Reti</button>
          <button class="button-77" v-on:click="goToTWEB">TWEB</button>
          
    </div>
    
    <h3>Essendo admin, puoi avere accesso a modifiche del modello</h3>

    <div class="eliminaProf">
    <h4>Rimuovi/Aggiungi professore</h4>
    <input type="text" v-model="professoreNome" placeholder="nome del professore"/>&nbsp;
    <input type="text" v-model="professoreCognome" placeholder="cognome del professore" />&nbsp;
    <button v-on:click="rimuoviProfessore" class="button-62">rimuovi docente</button>&nbsp;&nbsp;
    <button v-on:click="aggiungiProfessore" class="button-62">aggiungi docente</button>
    </div>

    <div class="eliminaCorso">
    <h4>Elimina/Aggiungi corso</h4>
    <input type="text" v-model="corso" placeholder="inserisci il nome del corso"/>&nbsp;
    <button v-on:click="rimuoviCorso" class="button-62">rimuovi corso</button>&nbsp;&nbsp;
    <button v-on:click="aggiungiCorso" class="button-62">aggiungi corso</button>
    </div>

    <div class="eliminaCorsoDocente">
    <h4>Elimina/Aggiungi associazione corso-docente</h4>
    <button v-on:click="rimuoviCorsoProfessore" class="button-62">rimuovi corso-docente</button>&nbsp;&nbsp;
    <button v-on:click="aggiungiCorsoProfessore" class="button-62">aggiungi corso-docente</button>
    </div>

    <br/><br/><br/><br/>

</template>


<script>
import axios from 'axios';
import config from "./configuration"
export default{
    name:"myHome",
    data(){
        return{
            MyUrl:'http://localhost:8080/Progetto1_war_exploded/servlet1',
            corso:'',
            professoreNome:'',
            professoreCognome:''
        }
    },
    methods:{
    SeeRepetitions()
    {
        this.$router.push({name:'HistoryAdmin'}) //qui andiamo nella History
    },

    LogOut()
    {
        config.GlobalLogin = false
        this.$router.push({name:'Login'}) //qui andiamo nella Login
    },

    rimuoviProfessore(){
        let self = this
        if(self.professoreNome === "" || self.professoreCognome === ""){
            alert("non hai inserito il cognome o nome del professore")
        }else{
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "removeProfessore",
                    profNome: this.professoreNome,
                    profCogn: this.professoreCognome
                }
            }).then(function(response){
                alert("Professore rimosso correttamente : " + response.data)
            })
        }
    },

    aggiungiProfessore(){
        let self = this
        if(self.professoreNome === "" || self.professoreCognome === ""){
            alert("non hai inserito il cognome o nome del professore")
        }else{
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "addProfessore",
                    profNome: this.professoreNome,
                    profCogn: this.professoreCognome
                }
            }).then(function(response){
                alert("Professore aggiunto correttamente : " + response.data)
            })
        }
    },
    
    rimuoviCorso(){
        let self = this
        if(self.corso === ""){
            alert("non hai inserito il nome del corso da rimuovere")
        }else{
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "removeCorso",
                    corsoNome : self.corso
                }
            }).then(function(response){
                alert("Corso rimosso correttamente : " + response.data)
            })
        }
    },

    aggiungiCorso(){
        let self = this
        if(self.corso === ""){
            alert("non hai inserito il nome del corso da aggiungere")
        }else{
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "addCorso",
                    corsoNome : self.corso
                }
            }).then(function(response){
                alert("Corso aggiunto correttamente : " + response.data)
            })
        }
    },

    rimuoviCorsoProfessore(){
        let self = this
        var value =`{"nome":"${self.professoreNome}","cognome":"${self.professoreCognome}"}`
        var myObject = JSON.parse(value);
        if(self.corso === "" || self.professoreCognome === "" || self.professoreNome === ""){
            alert("Per favore, compila tutti i campi")
        }else{
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "removeInsegnamento",
                    cda : self.corso,
                    pda : myObject

                }
            }).then(function(response){
                alert("Associazione Professore->Corso rimosso correttamente : " + response.data)
            })
        }
    },

    aggiungiCorsoProfessore(){
        let self = this
        var value =`{"nome":"${self.professoreNome}","cognome":"${self.professoreCognome}"}`
        var myObject = JSON.parse(value);
        var valueCorso = `{"corso":"${self.corso}"}`
        var myObjectCorso = JSON.parse(valueCorso);
        if(self.corso === "" || self.professoreCognome === "" || self.professoreNome === ""){
            alert("Per favore, compila tutti i campi")
        }else{
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "addInsegnamento",
                    cda : myObjectCorso,
                    pda : myObject

                }
            }).then(function(response){
                alert("Associazione Professore->Corso aggiunto correttamente : " + response.data)
            })
        }
    },

    goToIUM(){
        let self = this
        self.$router.push({name:'IUM'}) //qui andiamo nella History
    },
    goToTWEB(){
        let self = this
        self.$router.push({name:'TWEB'}) //qui andiamo nella History
    },
    goToReti(){
        let self = this
        self.$router.push({name:'Reti'}) //qui andiamo nella History
    },
    goToProg3(){
        let self = this
        self.$router.push({name:'Prog3'}) //qui andiamo nella History
    },
    goToProg2(){
        let self = this
        self.$router.push({name:'Prog2'}) //qui andiamo nella History
    },
    goToTutti(){
        let self = this
        self.$router.push({name:'Tutti'}) //qui andiamo nella History
    }
    }
    
}
</script>



