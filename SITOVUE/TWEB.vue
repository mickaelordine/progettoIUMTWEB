<template>
    <div class="menu">
        <button v-on:click="comeBackHome" v-if="!seiAdmin()" class="button-5">Torna alla Home</button>
        <button v-on:click="comeBackHomeAdmin" v-if="seiAdmin()" class="button-5">Torna alla HomeAdmin</button>
    </div>
    <br/><br/><br/>
    <div class="table">
    <br/>
    <h3>TWEB</h3>
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

        refresh(){
            let self = this
            var value =`{"corso":"tweb"}`
            axios.post(self.MyUrl,{}, {
                params: {
                    action : "viewFiltered",
                    filter : value
                }
            }).then(function(response){
                self.professori = response.data
            })
        }, 
        
        seiAdmin(){
            if(config.GlobalAdmin === true){
                return true
            }else {
                return false
            }
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


<style>
.icon{
    width: 50px
}

.button-5 {
  align-items: center;
  background-clip: padding-box;
  background-color: #fa6400;
  border: 1px solid transparent;
  border-radius: .25rem;
  box-shadow: rgba(0, 0, 0, 0.02) 0 1px 3px 0;
  box-sizing: border-box;
  color: #fff;
  cursor: pointer;
  display: inline-flex;
  font-family: system-ui,-apple-system,system-ui,"Helvetica Neue",Helvetica,Arial,sans-serif;
  font-size: 16px;
  font-weight: 600;
  justify-content: center;
  line-height: 1.25;
  margin: 0;
  min-height: 3rem;
  padding: calc(.875rem - 1px) calc(1.5rem - 1px);
  position: relative;
  text-decoration: none;
  transition: all 250ms;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
  vertical-align: baseline;
  width: auto;
}

.button-5:hover,
.button-5:focus {
  background-color: #fb8332;
  box-shadow: rgba(0, 0, 0, 0.1) 0 4px 12px;
}

.button-5:hover {
  transform: translateY(-1px);
}

.button-5:active {
  background-color: #c85000;
  box-shadow: rgba(0, 0, 0, .06) 0 2px 4px;
  transform: translateY(0);
}
</style>