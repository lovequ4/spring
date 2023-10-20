<template>
    <div class="q-pa-md" >
        <div class="form-container">
            <q-form
            @submit="onSubmit"
            @reset="onReset"
            class="q-gutter-md"
            >
            <div class="label" >SignIn</div>
            <q-input
                filled
                v-model="nameoremail"
                label="Your Email or Name*"
                type="text"
                hint="email"
                lazy-rules
                :rules="[ val => val && val.length > 0 || 'Please Enter your Email or Name']"
            />
            
            <q-input
                filled
                v-model="password"
                label="Your Password *"
                hint="password"
                type="password"
                lazy-rules
                :rules="[ val => val && val.length > 0 || 'Please Enter your Password']"
            />
            
            <div>
                <q-btn label="Submit" type="submit" color="primary"/>
                <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
            </div>

            <div class="link">
                <p>Don’t have an account? <router-link style="margin-left: 2%;" to='/signup'>SignUp</router-link></p>
            </div>
            </q-form>
        </div>
    </div>
</template>

<script>
import { useQuasar } from 'quasar'
import { ref } from 'vue'
import router from '../router'
import axios from 'axios'

export default {
  setup () {
    const $q = useQuasar()

    const nameoremail = ref(null)
    const password = ref(null)
    

    return {
      nameoremail,
      password,
      

      onSubmit () {
        const input = nameoremail.value; // Get the user input from your form
        
        const formData = {};

        if (isEmail(input)) {
            formData.email = input;
            formData.password = password.value;
        } else {
            formData.name = input;
            formData.password = password.value;
        }
      
        function isEmail(input) {
            // This is a simple email validation function. You may want to use a more robust validation method.
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(input);
        }
        console.log(formData)

        axios.post('http://localhost:8080/api/accounts/signin', formData)
          .then(response => {
            
            console.log(response)

            localStorage.setItem('token', response.data);
            
            $q.notify({
              color: 'green-4',
              textColor: 'white',
              icon: 'cloud_done',
              message: 'Submitted'
            })
            
            router.push({ name: 'index' });
          })
          .catch(error => {
            console.error(error)
            $q.notify({
              color: 'red-5',
              textColor: 'white',
              icon: 'warning',
              message: 'submit error'
            })
          })
      },

      onReset () {
        nameoremail.value = null
        password.value = null
        
      }
    }
  }
}
</script>

<style scoped>
.q-pa-md{
   margin-top: 10%;
}

.label{
    font-size:xx-large;
    margin-bottom: 5%;
}

.link{
    margin-top: 10%;
}

.form-container {
  border: 1px solid #ccc; /* 設置外框樣式 */
  padding: 20px; /* 設置內邊距 */
  text-align: center; /* 文字置中 */
  margin: 0 auto; /* 自動置中 */
  max-width: 400px; /* 最大寬度 */
  background-color: #f5f5f5;
  border-radius: 5px;
  box-shadow: 0px 5px 10px #dadadacc;
}
</style>