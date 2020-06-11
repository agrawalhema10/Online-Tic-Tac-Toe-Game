package com.airubiya.onlinetictactoe



import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // Database Instance
    private var database= FirebaseDatabase.getInstance()
    private var myref=database.reference


    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    var myEmail:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var b:Bundle?=intent.extras
        myEmail=b!!.getString("email")
        IncomingCalls()
    }
    fun buClick(view:View){
        var buSelected=view as Button
        var cellId=0
        when(buSelected.id){

            R.id.b1->cellId=1
            R.id.b2->cellId=2
            R.id.b3->cellId=3
            R.id.b4->cellId=4
            R.id.b5->cellId=5
            R.id.b6->cellId=6
            R.id.b7->cellId=7
            R.id.b8->cellId=8
            R.id.b9->cellId=9
        }
        myref.child("playOnline").child(sessionID!!).child(cellId.toString()).setValue(myEmail)
    }
    var activePlayer=1
    var player1=ArrayList<Int>()
    var player2=ArrayList<Int>()
     fun playGame(cellID:Int,buSelected:Button){
        if(activePlayer==1){
            buSelected.text="X"
            buSelected.setBackgroundResource(R.color.blue)
            player1.add(cellID)
            activePlayer=2
        }
        else{
            buSelected.text="0"
            buSelected.setBackgroundResource(R.color.darkGreen)
            player2.add(cellID)
            activePlayer=1
        }
        buSelected.isEnabled=false
        checkWinner()
    }
     fun checkWinner(){
        var winner=-1
//        row 1
        if (player1.contains(1) && player1.contains(2) && player1.contains(3)){
            winner=1
        }
        if (player2.contains(1) && player2.contains(2) && player2.contains(3)){
            winner=2
        }

//        row2

        if (player1.contains(4) && player1.contains(5) && player1.contains(6)){
            winner=1
        }
        if (player2.contains(4) && player2.contains(5) && player2.contains(6)){
            winner=2
        }

//        row3
        if (player1.contains(7) && player1.contains(8) && player1.contains(9)){
            winner=1
        }
        if (player2.contains(7) && player2.contains(8) && player2.contains(9)){
            winner=2
        }

//        Diagonal 1

        if (player1.contains(1) && player1.contains(5) && player1.contains(9)){
            winner=1
        }
        if (player2.contains(1) && player2.contains(5) && player2.contains(9)){
            winner=2
        }

//        Diagonal 2

        if (player1.contains(3) && player1.contains(5) && player1.contains(7)){
            winner=1
        }
        if (player2.contains(3) && player2.contains(5) && player2.contains(7)){
            winner=2
        }

//        Column1

        if (player1.contains(1) && player1.contains(4) && player1.contains(7)){
            winner=1
        }
        if (player2.contains(1) && player2.contains(4) && player2.contains(7)){
            winner=2
        }

//         Column 2
        if (player1.contains(2) && player1.contains(5) && player1.contains(8)){
            winner=1
        }
        if (player2.contains(2) && player2.contains(5) && player2.contains(8)){
            winner=2
        }

//          Column 3
        if (player1.contains(3) && player1.contains(6) && player1.contains(9)){
            winner=1
        }
        if (player2.contains(3) && player2.contains(6) && player2.contains(9)){
            winner=2
        }
        if(winner!=-1) {
            if (winner == 1) {
                Toast.makeText(this, "Player1 win the game", Toast.LENGTH_LONG).show()

            } else{
                Toast.makeText(this, "Player2 win the game", Toast.LENGTH_LONG).show()


            }
        }

    }
    fun AutoPlay(cellID:Int){
        var buSelect:Button?
        when(cellID){
            1-> buSelect=b1
            2-> buSelect=b2
            3-> buSelect=b3
            4-> buSelect=b4
            5-> buSelect=b5
            6-> buSelect=b6
            7-> buSelect=b7
            8-> buSelect=b8
            9-> buSelect=b9
            else->{
                buSelect=b1
            }
        }

        playGame(cellID,buSelect)
    }

    fun buAcceptEvent(view: View) {
        var userAemail=mail.text.toString()
        myref.child("Users").child(SplitString(userAemail)).child("Request").push().setValue(myEmail)
        playOnline(SplitString(userAemail!!)+SplitString(myEmail!!))
        playerSymbol="0"
    }
    fun buRequestEvent(view: View) {
        var userRemail=mail.text.toString()
        myref.child("Users").child(SplitString(userRemail)).child("Request").push().setValue(myEmail)
        playOnline(SplitString(myEmail!!)+SplitString(userRemail!!))
        playerSymbol="X"
    }
    var sessionID:String?=null
    var playerSymbol:String?=null
    fun playOnline(sessionID:String){
        this.sessionID=sessionID
        myref.child("playOnline").removeValue()


        
        myref.child("playOnline").child(sessionID).addValueEventListener(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                try{
                    player1.clear()
                    player2.clear()
                    val td=p0!!.value as HashMap<String,Any>
                    if(td!=null){
                        var value:String
                        for (key in td.keys){
                            value=td[key] as String
                            if(value!=myEmail){
                             activePlayer=if (playerSymbol=="X") 1 else 2
                            }
                            else{
                                activePlayer=if(playerSymbol=="X") 2 else 1
                            }
                            AutoPlay(key.toInt()
                            )
                            break




                        }
                    }


                }
                catch (ex:Exception){

                }
            }

        })

    }
    var number=0
    fun IncomingCalls(){
        myref.child("Users").child(SplitString(myEmail!!)).child("Request").addValueEventListener(object :ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
            try{
                val td=p0!!.value as HashMap<String,Any>
                if(td!=null){
                    var value:String
                    for (key in td.keys){
                        value=td[key] as String

                        mail.setText(value)
                        val notifyme=Notifications()
                        notifyme.Notify(applicationContext,value+"wnat to paly tictactoe",number)
                        number++

                        myref.child("Users").child(SplitString(myEmail!!)).child("Request").setValue(true)
                        break

                    }
                }


            }
            catch (ex:Exception){

            }
            }

        })

        }
    fun SplitString(str:String): String {
        var split=str.split("@")
        return split[0]
    }


}

