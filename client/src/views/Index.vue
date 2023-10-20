<template>
    <q-card>
      <q-card-section>
        <div class="text-h5">{{userName}} balance</div>
      </q-card-section>
      <q-card-section>
        <div class="text-h4">{{ amount }}</div>
      </q-card-section>
    </q-card>

    <q-table
     title="Transfer Record"
     :rows="rows"
     :columns="columns"
     row-key="name"
     style="margin-top: 2%;"
     >

      <template v-slot:top-right="props">
          <q-btn icon="price_change" color="primary" @click="showTransferDialog = true"  />
      </template>

      <template v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn icon="cancel" @click="onDelete(props.row)" v-if="props.row.status === 'Padding' "></q-btn>
          </q-td>
      </template>

    </q-table>

      <!-- add transfer Dialog -->
     <q-dialog v-model="showTransferDialog">
       <q-card style="width: 500px; max-width: 200vw;">
          <q-card-section>
            <div class="text-h5">Transfer</div>
            <q-input v-model="newTransfer.recipientAccountId" label="Recipient Account ID"></q-input>
            <q-input v-model="newTransfer.transferAmount" label="Transfer Amount" type="number"></q-input>
            <q-input v-model="newTransfer.description" label="Description"></q-input>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn label="Create" color="primary" @click="onAddTransfer"></q-btn>
            <q-btn label="Cancel" color="negative" @click="onCancel"></q-btn>
          </q-card-actions>
        </q-card>
     </q-dialog>


      <!-- Cancel Dialog -->
      <q-dialog v-model="showCancelDialog">
        <q-card>
            <q-card-section>
            <q-card-title class="text-h5">Warning</q-card-title>
            <div class="text-h6">Are you sure you want to Cancel this?</div>
            </q-card-section>
            <q-card-actions align="right">
            <q-btn label="Delete" color="negative" @click="confirmDelete"></q-btn>
            <q-btn label="Cancel" color="primary" @click="cancelDelete"></q-btn>
            </q-card-actions>
        </q-card>
        
    </q-dialog>

</template> 
 

<script>
 import axios from 'axios'
 import jwt_decode from 'jwt-decode'
 import { format,  parseISO } from 'date-fns';

 export default {
    data() {
     return {
       columns: [
          { name: 'index', label: 'Index', align: 'left', field: 'index', sortable: true },
          { name: 'recipientAccount', label: 'RecipientAccount', align: 'center', field: 'recipientAccountId' , sortable: true },
          { name: 'transferAmount', label: 'transferAmount', align: 'center', field: 'transferAmount', sortable: true },
          { name: 'description', label: 'Description', align: 'center', field: 'description', sortable: true },
          { name: 'createdTime', label: 'CreatedTime', align: 'center', field: (row)=>format(parseISO(row.createdTime), 'yyyy/MM/dd HH:mm') , sortable: true },
          { name: 'endTime', label: 'EndTime', align: 'center', field: (row)=>format(parseISO(row.endTime), 'yyyy/MM/dd HH:mm') , sortable: true },
          { name: 'status', label: 'Status', align: 'center', field: 'status', sortable: true },
          { name: 'actions', label: 'Action'}
       ], 
       rows: [],
       amount:null,
       showTransferDialog: false,
       medium:false,
       newTransfer: {},
       userId:null,
       userName:null,
       showCancelDialog:false,
       rowToCancel:null,
     };
   },
   mounted() {
    const token = localStorage.getItem('token');
    const headers = {
            'Authorization': `Bearer ${token}`  
        };
  

    if (token) {
        const decodedToken = jwt_decode(token);
        this.userId = decodedToken.id;
        this.userName = decodedToken.sub;
    } else {
        return null;
    }
      axios.get(`http://localhost:8080/api/accounts/${this.userId}`,{headers})
     
       .then(response => {
         console.log(response.data);
         this.amount = response.data;
       })
       .catch(error => {
         console.error(error);
       });

      axios.get(`http://localhost:8080/api/transfer/${this.userId}`)
      .then(response => {
        console.log(response.data);
        const transferData = response.data.map(row => {
        return {
            ...row,
          };
        });
        this.rows = transferData;
        this.rows = transferData.map((row, index) => ({ ...row, index: index + 1 }));
      })
      .catch(error => {
        console.error(error);
      }); 
   }, 
   methods: {
    
    onAddTransfer() { 
      console.log(this.userId)
      const Data = {
         senderAccount:{id:this.userId},
         recipientAccount: {id:this.newTransfer.recipientAccountId},
         transferAmount: this.newTransfer.transferAmount,
         description: this.newTransfer.description,
      };
      
      console.log(Data)
      axios.post('http://localhost:8080/api/transfer', Data)
      
        .then(response => {
          console.log('Data :', response.data);
          this.showTransferDialog = false;
          this.refreshData();
        })
        .catch(error => {
          console.error('Error data:', error);
        });
    },

    onCancel() {
       this.showTransferDialog = false;
    },

    //cancel
    onDelete(row){
      this.rowToCancel = row;
      this.showCancelDialog=true;
    },

    confirmDelete(){
      console.log(typeof(this.rowToCancel.transferId))
      axios.post(`http://localhost:8080/api/transfer/${this.rowToCancel.transferId}`)       
      .then(response => {
        console.log("cancel",response.data);
        this.showCancelDialog = false;
        this.refreshData();
      })
      .catch(error => {
        console.error('Error cancel  data:', error);
      })
      this.showCancelDialog = false;
    },

    cancelDelete(){
      this.showCancelDialog=false;
    },

    //refresh
    refreshData() {
        axios.get(`http://localhost:8080/api/transfer/${this.userId}`)
        .then(response => {
          console.log(response.data);
          const transferData = response.data.map(row => {
          return {
              ...row,
            };
          });
          this.rows = transferData;
          this.rows = transferData.map((row, index) => ({ ...row, index: index + 1 }));
        })
        .catch(error => {
          console.error(error);
        });
    }
  }
}

</script>
 
 
<style>
 .my-table-details {
   font-size: 0.85em;
   font-style: italic;
   max-width: 200px;
   white-space: normal;
   color: #555;
   margin-top: 4px;
 }

 </style>