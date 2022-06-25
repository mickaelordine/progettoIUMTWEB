<template>
    <h1>BENVENUTO NELLO STORICO, ADMIN</h1>
    <div class="menu">
        <button v-on:click="backHome" class="button-5">Torna alla Home</button>&nbsp;&nbsp;
        <button v-on:click="refresh" class="button-62">Refresh</button>
    </div>
    <div class="indice">
            <p style="text-align:right">Indice per lo status: &nbsp;</p>
            <p style="text-align:right">status = 0 => prenotabile &nbsp;</p>
            <p style="text-align:right">status = 1 => prenotata &nbsp;</p>
            <p style="text-align:right">status = 2 => non prenotabile &nbsp;</p>
            <p style="text-align:right">status = 3 => effettuata &nbsp;</p>
            <p style="text-align:right">status = 4 => disdetta &nbsp;</p>
    </div>
    <br/>
    <br/>
    <br/>
    <h3>qui puoi vedere le ripetizioni passate</h3>

    <div class="listaStorico">
    <ol>
        <li v-for="corso in professori" v-bind:value="corso.rip" v-bind:key="corso.id" id="lista">
        <p id="listaRipeProf" style="display:inline">
            {{corso.rip.prof}} &nbsp;
        </p>
        <p id="listaRipeCorso" style="display:inline">
            {{corso.rip.corso}} &nbsp;
        </p>
        <p id="listaRipeGiorno" style="display:inline">
            {{corso.rip.giorno}} &nbsp;
        </p>
        <p id="listaRipeOra" style="display:inline">
            {{corso.rip.ora}} &nbsp;
        </p>
        <p id="listaRipeStatus" style="display:inline">
            {{corso.rip.status}} &nbsp;
        </p>
        <button v-on:click="disdici(corso.rip.prof,corso.rip.corso,corso.rip.giorno,corso.rip.ora)" class="button-6" v-if="disdicibile(corso.rip.status)">Disdici</button>&nbsp;&nbsp;
        <button v-on:click="done(corso.rip.prof,corso.rip.corso,corso.rip.giorno,corso.rip.ora)" class="button-41">Segna come già fatto</button>
        <hr>
        </li>
    </ol>


    <h3>qui puoi vedere le ripetizioni passate degli altri utenti</h3>
    </div>
    <div class="listaStoricoAltri">
        <ol>
        <li v-for="corsoAltri in professoriAltri" v-bind:value="corsoAltri.rip" v-bind:key="corsoAltri.id" id="listaAltri">
        <p id="listaUtenteAltri" style="display:inline">
            {{corsoAltri.usr}} &nbsp;
        </p>
        <p id="listaRipeProfAltri" style="display:inline">
            {{corsoAltri.rip.prof}} &nbsp;
        </p>
        <p id="listaRipeCorsoAltri" style="display:inline">
            {{corsoAltri.rip.corso}} &nbsp;
        </p>
        <p id="listaRipeGiornoAltri" style="display:inline">
            {{corsoAltri.rip.giorno}} &nbsp;
        </p>
        <p id="listaRipeOraAltri" style="display:inline">
            {{corsoAltri.rip.ora}} &nbsp;
        </p>
        <hr>
        </li>
    </ol>
    </div>
</template>


<script>
import axios from 'axios'
import config from "./configuration"
export default{
    name : "myHistoryAdmin",
    data(){
        return{
            MyUrl:'http://localhost:8080/Progetto1_war_exploded/servlet1',
            professori : '',
            professoriAltri : '',
            corsoRip : ''
        }
    },
    methods:{
        backHome()
        {
            this.$router.push({name:'HomeAdmin'}) //qui andiamo nella History
        },
        
        refresh(){
            let self = this
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "myPrenots",
                }
            }).then(function(response){
                self.professori = response.data
            })

            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewAllPrenot",
                }
            }).then(function(response){
                self.professoriAltri = response.data
            })

        },
        disdici(prof,corso,giorno,ora){
            var value =`{"rip":{"corso":"${corso}","giorno":"${giorno}","ora":${ora},"prof":"${prof}"},"user":"${config.GlobalUsername}"}`
            var myObject = JSON.parse(value);
            let self = this
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "disdici",
                    pdd : myObject
                }
            }).then(function(response){
                alert(response.data)
                self.professori = response.data
            })
        },
        disdicibile(status){
            if(status === 4){
                return false
            } else {
                return true
            }
        }
    },
    beforeMount(){
    this.refresh()
    }
}
</script>