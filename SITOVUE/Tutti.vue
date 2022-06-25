<template>
    
    <div class="menu">
        <button v-on:click="comeBackHome" v-if="!seiAdmin()" class="button-5">Torna alla Home</button>
        <button v-on:click="comeBackHomeAdmin" v-if="seiAdmin()" class="button-5">Torna alla HomeAdmin</button>
    </div>
    <br/>
    <br/>
    <br/>
    <div class="table">
    <br/>
    <br/>
    <br/>
    <h3 style="text-align:center">Qui ci sono tutte le ripetizioni</h3>
    <button v-on:click="refresh" class="button-62">Refresh</button>

    <br/>
        <input type="text" v-model="nomeProf" placeholder="nome Prof"/>&nbsp;
        <input type="text" v-model="cognomeProf" placeholder="cognome Prof"/>&nbsp;
        <button v-on:click="refreshNome" class="button-62">Filtra per Prof</button><br/>
        <input type="text" v-model="corso" placeholder="corso"/>&nbsp;
        <button v-on:click="refreshCorso" class="button-62">Filtra per corso</button><br/>
        <input type="text" v-model="giorno" placeholder="giorno"/>&nbsp;
        <button v-on:click="refreshGiorno" class="button-62">Filtra per giorno</button><br/>
        <input type="text" v-model="ora" placeholder="ora"/>&nbsp;
        <button v-on:click="refreshOra" class="button-62">Filtra per ora</button><br/>
    <br/>

    <ul v-for="corso in professori" v-bind:value="corso.rip" v-bind:key="corso.id" id="lista">
        <p id="listaRipeProf" style="display:inline">
            {{corso.prof}} &nbsp;
        </p>
        <p id="listaRipeCorso" style="display:inline">
            {{corso.corso}} &nbsp;
        </p>
        <p id="listaRipeGiorno" style="display:inline">
            {{corso.giorno}} &nbsp;
        </p>
        <p id="listaRipeOra" style="display:inline">
            {{corso.ora}} &nbsp;
        </p>
        <button v-on:click="prenota(corso.prof,corso.corso,corso.giorno,corso.ora)" class="button-43">prenota</button>
        <hr>
    </ul>
    </div>
</template>

<script>
import axios from 'axios';
import config from "./configuration"
export default {
    name : 'myIUM',
    data()
    {
        return{
            MyUrl:'http://localhost:8080/Progetto1_war_exploded/servlet1',
            email:'',
            password:'',
            professori: '',

            //per la gestione della prenotazione 
            nomeProf:'',
            cognomeProf:'',
            corso:'',
            giorno: '',
            ora : ''

        }
    },
    methods:{
        comeBackHome(){
            let self = this;
            self.$router.push({name:'Home'})
        },

        comeBackHomeAdmin(){
            let self = this;
            if(config.GlobalAdmin === true){
                self.$router.push({name:'Home'})
            }else{
                alert("mi dispiace non sei un admin");
            }
            
        },

        refresh(){
            let self = this
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewPrenotabili"
                }
            }).then(function(response){
                self.professori = response.data
            })
        },
        refreshNome(){
            let self = this
            var value = `{"prof:"{"nome":"${self.nomeProf}","cognome:""${self.cognomeProf}"}"}`
            var myObject = JSON.parse(value);
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewFiltered",
                    filter : myObject
                }
            }).then(function(response){
                self.professori = response.data
            })
        },
        refreshCorso(){
            let self = this
            alert(self.corso)
            var value = `{"corso":"${self.corso}"}`
            var myObject = JSON.parse(value);
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewFiltered",
                    filter : myObject
                }
            }).then(function(response){
                self.professori = response.data
            })
        },
        refreshGiorno(){
            let self = this
            var value = `{"giorno":"${self.giorno}"}`
            var myObject = JSON.parse(value);
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewFiltered",
                    filter : myObject
                }
            }).then(function(response){
                self.professori = response.data
            })
        },
        refreshOra(){
            let self = this
            var value = `{"ora":"${self.ora}"}`
            var myObject = JSON.parse(value);
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewFiltered",
                    filter : myObject
                }
            }).then(function(response){
                self.professori = response.data
            })
        },

        prenota(prof,corso,giorno,ora){
            if(config.GlobalLogin === false){
                alert("Per poter effettuare una ripetizioni, prima effettua il login")
                let self = this
                self.$router.push({name:'Login'}) //qui andiamo nella History
                return false
            } 
            var value =`{"prof":"${prof}","corso":"${corso}","giorno":"${giorno}","ora":"${ora}"}`
            var myObject = JSON.parse(value);
            axios.post(this.MyUrl,{}, {
                params: {
                    action : "prenota",
                    rdp : myObject
                }
            }).then(function(response){
                alert(response.data)
            })
        },
        seiAdmin(){
            if(config.GlobalAdmin === true){
                return true
            }else {
                return false
            }
        }
    },
    beforeMount(){
    this.refresh()
 }
}
//VEDERE LA PARTTE DI COLLEGAMENTO AD UNA SERVLET
</script>


<style>

/* CSS */
.button-62 {
  background: linear-gradient(to bottom right, #EF4765, #FF9A5A);
  border: 0;
  border-radius: 12px;
  color: #FFFFFF;
  cursor: pointer;
  display: inline-block;
  font-family: -apple-system,system-ui,"Segoe UI",Roboto,Helvetica,Arial,sans-serif;
  font-size: 16px;
  font-weight: 500;
  line-height: 2.5;
  outline: transparent;
  padding: 0 1rem;
  text-align: center;
  text-decoration: none;
  transition: box-shadow .2s ease-in-out;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
  white-space: nowrap;
}

.button-62:not([disabled]):focus {
  box-shadow: 0 0 .25rem rgba(0, 0, 0, 0.5), -.125rem -.125rem 1rem rgba(239, 71, 101, 0.5), .125rem .125rem 1rem rgba(255, 154, 90, 0.5);
}

.button-62:not([disabled]):hover {
  box-shadow: 0 0 .25rem rgba(0, 0, 0, 0.5), -.125rem -.125rem 1rem rgba(239, 71, 101, 0.5), .125rem .125rem 1rem rgba(255, 154, 90, 0.5);
}
</style>