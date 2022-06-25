<template>
    <div class="menu">
        <button v-on:click="backHome" class="button-5">Torna alla Home</button>
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
    <h3>Qui puoi vedere le ripetizioni passate</h3>

    <div class="listaStorico">
    <ul v-for="corso in professori" v-bind:value="corso.rip" v-bind:key="corso.id" id="lista">
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
        <button v-on:click="disdici(corso.rip.prof,corso.rip.corso,corso.rip.giorno,corso.rip.ora,corso.rip.status)" class="button-6" v-if="disdicibile(corso.rip.status)">Disdici</button>&nbsp;&nbsp;
        <button v-on:click="done(corso.rip.prof,corso.rip.corso,corso.rip.giorno,corso.rip.ora,corso.rip.status)" class="button-41">Segna come già fatto</button>
        <hr>
    </ul>
    </div>
</template>


<script>
import axios from 'axios'
import config from "./configuration"
export default{
    name : "myHistory",
    data(){
        return{
            MyUrl:'http://localhost:8080/Progetto1_war_exploded/servlet1',
            professori : '',
            corsoRip : '',
            corsiPresenti: '',
            index : '',
        }
    },
    methods:{
        backHome()
        {
            this.$router.push({name:'Home'}) //qui andiamo nella History
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
        },

        disdici(prof,corso,giorno,ora){
            //{"rip":{"corso":"prog3","giorno":"LUN","ora":18,"prof":"ardissono","status":1},"user":"user"}
            var value =`{"rip":{"corso":"${corso}","giorno":"${giorno}","ora":${ora},"prof":"${prof}"},"user":"${config.GlobalUsername}"}`
            var myObject = JSON.parse(value);
            let self = this
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "disdici",
                    pdd : myObject
                }
            }).then(function(response){
                self.refresh()
                alert(response.data)
            })
        },
        done(prof,corso,giorno,ora){
            var value =`{"rip":{"corso":"${corso}","giorno":"${giorno}","ora":${ora},"prof":"${prof}"},"user":"${config.GlobalUsername}"}`
            var myObject = JSON.parse(value);
            let self = this
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "setDone", //da aggiustare nella servlet NON esiste
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