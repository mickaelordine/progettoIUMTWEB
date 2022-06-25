<template>
    <div class="menu">
        <button v-on:click="SeeOldRepetitions">Vedi Storico</button>
        <button v-on:click="LogOut">LogOut</button>
    </div>
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
    Submit()
    {
        if(this.giorno === "" || this.orario === "" || this.materia === "" || this.professore === ""){
            alert("per favore, seleziona almeno un elemento per campo");
        }else{
            //richiesta get per le ripetizioni
            alert(this.giorno + this.orario + this.materia + this.professore);
        }
    },
    SeeOldRepetitions()
    {
        let self = this
        self.$router.push({name:'History'}) //qui andiamo nella History
    },

    LogOut()
    {
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



