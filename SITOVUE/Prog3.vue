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
    <h3>Prog3</h3>
    <button v-on:click="refresh" class="button-62">Refresh</button>
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
            prof:'',
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
                self.$router.push({name:'HomeAdmin'})
            }else{
                alert("mi dispiace non sei un admin");
            }
            
        },
        seiAdmin(){
            if(config.GlobalAdmin === true){
                return true
            }else {
                return false
            }
        },

        refresh(){
            let self = this
            var value =`{"corso":"prog3"}`
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewFiltered",
                    filter : value
                }
            }).then(function(response){
                self.professori = response.data
            })
        }, 
        
        isDifferent(){
            var selectProf = document.getElementById("profId")
            for(let i = 1; i<selectProf.length; i++){
                if(selectProf.options[i].value === selectProf.options[i-1].value){
                    //alert(selectProf.options[0].value + selectProf.options[1].value)
                    selectProf.options[i].value = null
                }
            }
            return selectProf.options.data
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
        }
    },
    beforeMount(){
    this.refresh()
 }
}
//VEDERE LA PARTTE DI COLLEGAMENTO AD UNA SERVLET
</script>

