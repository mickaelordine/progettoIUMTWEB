<template>
    <div class="menu">
        <button v-on:click="SeeOldRepetitions" class="button-5">Vedi Storico</button>
        <button v-on:click="LogOut" class="button-6">LogOut</button>
    </div>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <h1>Welcome to the HomePage</h1>
    <hr>
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
</template>


<script>
import axios from 'axios';
import config from "./configuration"
export default{
    name:"myHome",
    data(){
        return{
            MyUrl:'http://localhost:8080/Progetto1_war_exploded/servlet1',
            professore:'',
            materia:'',
            orario:'',
            giorno:'',
            corsi:[]
        }
    },
    methods:{
    SeeOldRepetitions()
    {
        let self = this
        self.$router.push({name:'History'}) //qui andiamo nella History
    },

    LogOut()
    {
        config.GlobalLogin = false
        let self = this
        self.$router.push({name:'Login'}) //qui andiamo nella LOGIN
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
    },
    components:{
        async mounted()
    {
        const db_path = './src/assets/db.json';

        let result = axios.get(db_path);
        console.warn(result);
        //controlla qui
        this.corsi = result.data;
    }
    } 
}
}


</script>



<style>
.menu{
    margin-top: 0px;
    float: left;
    overflow: hidden;
    text-align: center;
    font-size: 17px;
    text-decoration: none;
    padding: 14px 16px;
    margin-right: 5px;
}
.tendina{
    margin-top: 0px;
    align-content: center;
    float: left;
    overflow: hidden;
    text-align: center;
    font-size: 17px;
    padding: 14px 16px;
    border:1px solid black;
    background: white;
}
.submit{
    width: 320px;
    height: 40px;
    border: 1px solid skyblue;
    background-color: skyblue;
}

body{
    
    background-position: bottom;
    background-color:aquamarine;
}


.button-78 {
  align-items: center;
  appearance: none;
  background-clip: padding-box;
  background-color: initial;
  background-image: none;
  border-style: none;
  box-sizing: border-box;
  color: #fff;
  cursor: pointer;
  display: inline-block;
  flex-direction: row;
  flex-shrink: 0;
  font-family: Eina01,sans-serif;
  font-size: 16px;
  font-weight: 800;
  justify-content: center;
  line-height: 24px;
  margin: 0;
  min-height: 64px;
  outline: none;
  overflow: visible;
  padding: 60px 90px;
  pointer-events: auto;
  position: relative;
  text-align: center;
  text-decoration: none;
  text-transform: none;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
  vertical-align: middle;
  width: auto;
  word-break: keep-all;
  z-index: 0;
}

@media (min-width: 768px) {
  .button-78 {
      padding: 60px 90px;
  }
}

.button-78:before,
.button-78:after {
  border-radius: 80px;
}

.button-78:before {
  background-image: linear-gradient(92.83deg, #ff7426 0, #f93a13 100%);
  content: "";
  display: block;
  height: 100%;
  left: 0;
  overflow: hidden;
  position: absolute;
  top: 0;
  width: 100%;
  z-index: -2;
}

.button-78:after {
  background-color: initial;
  background-image: linear-gradient(#541a0f 0, #0c0d0d 100%);
  bottom: 4px;
  content: "";
  display: block;
  left: 4px;
  overflow: hidden;
  position: absolute;
  right: 4px;
  top: 4px;
  transition: all 100ms ease-out;
  z-index: -1;
}

.button-78:hover:not(:disabled):before {
  background: linear-gradient(92.83deg, rgb(255, 116, 38) 0%, rgb(249, 58, 19) 100%);
}

.button-78:hover:not(:disabled):after {
  bottom: 0;
  left: 0;
  right: 0;
  top: 0;
  transition-timing-function: ease-in;
  opacity: 0;
}

.button-78:active:not(:disabled) {
  color: #ccc;
}

.button-78:active:not(:disabled):before {
  background-image: linear-gradient(0deg, rgba(0, 0, 0, .2), rgba(0, 0, 0, .2)), linear-gradient(92.83deg, #ff7426 0, #f93a13 100%);
}

.button-78:active:not(:disabled):after {
  background-image: linear-gradient(#541a0f 0, #0c0d0d 100%);
  bottom: 4px;
  left: 4px;
  right: 4px;
  top: 4px;
}

.button-78:disabled {
  cursor: default;
  opacity: .24;
}


.button-77 {
  align-items: center;
  appearance: none;
  background-clip: padding-box;
  background-color: initial;
  background-image: none;
  border-style: none;
  box-sizing: border-box;
  color: #fff;
  cursor: pointer;
  display: inline-block;
  flex-direction: row;
  flex-shrink: 0;
  font-family: Eina01,sans-serif;
  font-size: 16px;
  font-weight: 800;
  justify-content: center;
  line-height: 24px;
  margin: 0;
  min-height: 64px;
  outline: none;
  overflow: visible;
  padding: 50px 80px;
  pointer-events: auto;
  position: relative;
  text-align: center;
  text-decoration: none;
  text-transform: none;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
  vertical-align: middle;
  width: auto;
  word-break: keep-all;
  z-index: 0;
}

@media (min-width: 768px) {
  .button-77 {
    padding: 50px 80px;
  }
}

.button-77:before,
.button-77:after {
  border-radius: 80px;
}

.button-77:before {
  background-color: rgba(249, 58, 19, .32);
  content: "";
  display: block;
  height: 100%;
  left: 0;
  overflow: hidden;
  position: absolute;
  top: 0;
  width: 100%;
  z-index: -2;
}

.button-77:after {
  background-color: initial;
  background-image: linear-gradient(92.83deg, #ff7426 0, #f93a13 100%);
  bottom: 4px;
  content: "";
  display: block;
  left: 4px;
  overflow: hidden;
  position: absolute;
  right: 4px;
  top: 4px;
  transition: all 100ms ease-out;
  z-index: -1;
}

.button-77:hover:not(:disabled):after {
  bottom: 0;
  left: 0;
  right: 0;
  top: 0;
  transition-timing-function: ease-in;
}

.button-77:active:not(:disabled) {
  color: #ccc;
}

.button-77:active:not(:disabled):after {
  background-image: linear-gradient(0deg, rgba(0, 0, 0, .2), rgba(0, 0, 0, .2)), linear-gradient(92.83deg, #ff7426 0, #f93a13 100%);
  bottom: 4px;
  left: 4px;
  right: 4px;
  top: 4px;
}

.button-77:disabled {
  cursor: default;
  opacity: .24;
}

/* button per tutti i corsi */
.button-6 {
  align-items: center;
  background-clip: padding-box;
  background-color: rgb(255, 0, 0);
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

.button-6:hover,
.button-6:focus {
  background-color: rgb(255, 0, 0);
  box-shadow: rgba(0, 0, 0, 0.1) 0 4px 12px;
}

.button-6:hover {
  transform: translateY(-1px);
}

.button-6:active {
  background-color: #540f0f;
  box-shadow: rgba(0, 0, 0, .06) 0 2px 4px;
  transform: translateY(0);
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


/* CSS */
.button-43 {
  background-image: linear-gradient(-180deg, #37AEE2 0%, #1E96C8 100%);
  border-radius: .5rem;
  box-sizing: border-box;
  color: #FFFFFF;
  display: inline-flex;
  padding: calc(.875rem - 1px) calc(1.5rem - 1px);
  font-size: 16px;
  justify-content: center;
  padding: 3px 5px;
  align-content: center;
  text-decoration: none;
  width: 5%;
  border: 0;
  cursor: pointer;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
}

.button-43:hover {
  background-image: linear-gradient(-180deg, #1D95C9 0%, #17759C 100%);
}

@media (min-width: 768px) {
  .button-43 {
    padding: 3px 5px;
  }
}


/* CSS */
.button-41 {
  background-color: initial;
  background-image: linear-gradient(-180deg, #00D775, #00BD68);
  border-radius: 5px;
  box-shadow: rgba(0, 0, 0, 0.1) 0 2px 4px;
  color: #FFFFFF;
  cursor: pointer;
  display: inline-block;
  font-family: Inter,-apple-system,system-ui,Roboto,"Helvetica Neue",Arial,sans-serif;
  height: 50px;
  line-height: 30px;
  outline: 0;
  overflow: hidden;
  padding: 0 5px;
  pointer-events: auto;
  position: relative;
  text-align: center;
  touch-action: manipulation;
  user-select: none;
  -webkit-user-select: none;
  vertical-align: top;
  white-space: nowrap;
  width: 10%;
  z-index: 9;
  border: 0;
}

.button-41:hover {
  background: #00bd68;
}
</style>