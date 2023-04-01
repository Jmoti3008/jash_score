package ca.jashmotisariya.jash_score

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    // All Views Variables
    private lateinit var teamOneScore: TextView
    private lateinit var teamTwoScore: TextView
    private lateinit var matchInfo: TextView
    private lateinit var scoringOptions: RadioGroup
    private lateinit var teamOneCard: MaterialCardView
    private lateinit var teamTwoCard: MaterialCardView
    private lateinit var btnIncrease: Button
    private lateinit var btnDecrease: Button
    private lateinit var btnNewGame: Button

    // team score variables
    private var teamOneRun = 0
    private var teamOneWicket = 0
    private var teamTwoRun = 0
    private var teamTwoWicket = 0

    // Some condition variables
    private var isAnyTeamSelected = false
    private var currentSelectedCard = -1

    // Enum class to keep the track of which action is happening currently,
    // required to differentiate between increase and decrease
    enum class Action {
        NONE, INCREASE, DECREASE
    }

    private var btnAction = Action.NONE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
        setupClicks()
    }

    //  For initializing all the views
    private fun setupViews() {
        teamOneCard = findViewById(R.id.teamOneCard)
        teamTwoCard = findViewById(R.id.teamTwoCard)
        btnIncrease = findViewById(R.id.btnIncrease)
        btnDecrease = findViewById(R.id.btnDecrease)
        btnNewGame = findViewById(R.id.btnNewGame)
        teamOneScore = findViewById(R.id.teamOneScore)
        teamTwoScore = findViewById(R.id.teamTwoScore)
        matchInfo = findViewById(R.id.matchInfo)
        scoringOptions = findViewById(R.id.ScoringOptions)
        updateScores()
    }

    // for setting up all the clicks in the app
    private fun setupClicks() {
        teamOneCard.setOnClickListener {
            isAnyTeamSelected = true
            currentSelectedCard = 1
            updateScores()
        }
        teamTwoCard.setOnClickListener {
            isAnyTeamSelected = true
            currentSelectedCard = 2
            updateScores()
        }
        btnIncrease.setOnClickListener {
            btnAction = Action.INCREASE
            when (currentSelectedCard) {
                1 -> updateTeamOneScore()
                2 -> updateTeamTwoScore()
            }
        }

        btnDecrease.setOnClickListener {
            btnAction = Action.DECREASE
            when (currentSelectedCard) {
                1 -> updateTeamOneScore()
                2 -> updateTeamTwoScore()
            }
        }
        btnNewGame.setOnClickListener {
            currentSelectedCard = -1
            resetScores()
            updateScores()
        }
    }

    // to reset the scores when user press new game
    private fun resetScores() {
        scoringOptions.clearCheck()
        teamOneRun = 0
        teamOneWicket = 0
        teamTwoWicket = 0
        teamTwoWicket = 0
    }

    // update score function which called everytime when the user clicks INCREASE/DECREASE
    private fun updateScores() {
        updateIncreaseDecreaseButton()
        btnNewGame.isVisible =
            (teamOneRun > 0 || teamTwoRun > 0 || teamOneWicket > 0 || teamTwoWicket > 0)
        matchInfo.text =
            if (currentSelectedCard == -1) getString(R.string.game_start) else getString(R.string.game_started)
    }

    //    Function to update team one's score when user has selected
    //    the card one and performing the actions
    private fun updateTeamOneScore() {
        if (scoringValue != -1) {
            when (btnAction) {
                Action.NONE -> {}
                Action.INCREASE -> teamOneRun += scoringValue
                Action.DECREASE -> teamOneRun -= scoringValue
            }

        } else {
            when (btnAction) {
                Action.NONE -> {}
                Action.INCREASE -> teamOneWicket += 1
                Action.DECREASE -> teamOneWicket -= 1
            }
        }
        teamOneScore.text = getScore(teamOneRun, teamOneWicket)
        updateIncreaseDecreaseButton()
    }

    //    Function to update team two's score when user has selected
    //    the card two and performing the actions
    private fun updateTeamTwoScore() {
        if (scoringValue != -1) {
            when (btnAction) {
                Action.NONE -> {}
                Action.INCREASE -> teamTwoRun += scoringValue
                Action.DECREASE -> teamTwoRun -= scoringValue
            }
        } else {
            when (btnAction) {
                Action.NONE -> {}
                Action.INCREASE -> teamTwoWicket += 1
                Action.DECREASE -> teamTwoWicket -= 1
            }
        }
        teamTwoScore.text = getScore(teamTwoRun, teamTwoWicket)
        updateIncreaseDecreaseButton()
    }

    //    To update the status of INCREASE/DECREASE button,
//    like when the RUN is 0, decrease should not work
    private fun updateIncreaseDecreaseButton() {
        when (currentSelectedCard) {
            1 -> {
                selectTeamCardOne()
                btnIncrease.isEnabled = teamOneWicket < 10
                btnDecrease.isEnabled = teamOneRun > 0 && teamOneWicket < 10
            }
            2 -> {
                selectTeamCardTwo()
                btnIncrease.isEnabled = teamTwoWicket < 10
                btnDecrease.isEnabled = teamTwoRun > 0 && teamTwoWicket < 10
            }
            -1 -> {
                resetTeamCardSelection()
                btnIncrease.isEnabled = false
                btnDecrease.isEnabled = false
            }
        }
    }

    // Function to show stroke around the card 1 and disable around the card 2
    private fun selectTeamCardOne() {
        teamOneCard.apply {
            setStrokeColor(ColorStateList.valueOf(getColor(R.color.radioButtonWicket)))
            strokeWidth = 10
        }
        teamTwoCard.apply {
            setStrokeColor(ColorStateList.valueOf(getColor(R.color.app_window_over_color)))
            strokeWidth = 0
        }
    }

    // Function to show stroke around the card 2 and disable around the card 1
    private fun selectTeamCardTwo() {
        teamTwoCard.apply {
            setStrokeColor(ColorStateList.valueOf(getColor(R.color.radioButtonWicket)))
            strokeWidth = 10
        }
        teamOneCard.apply {
            setStrokeColor(ColorStateList.valueOf(getColor(R.color.app_window_over_color)))
            strokeWidth = 0
        }
    }

    // Function to disable both the stroke border around both cards
    private fun resetTeamCardSelection() {
        teamOneCard.apply {
            setStrokeColor(ColorStateList.valueOf(getColor(R.color.app_window_over_color)))
            strokeWidth = 0
        }
        teamTwoCard.apply {
            setStrokeColor(ColorStateList.valueOf(getColor(R.color.app_window_over_color)))
            strokeWidth = 0
        }
    }

    //    Decide the scoring value according to the (Scoring Option )radio button selected
    private val scoringValue: Int
        get() {
            val scoreValue: Int = when (scoringOptions.checkedRadioButtonId) {
                R.id.ScoreOne -> 1
                R.id.ScoreTwo -> 2
                R.id.ScoreThree -> 3
                R.id.ScoreFour -> 4
                R.id.ScoreFive -> 6
                R.id.Wicket -> -1
                else -> 0
            }
            return scoreValue
        }

    //    Function to pass the Run and Wickets both and get a
//    String output of it to set in the textview
    private fun getScore(run: Int, wicket: Int): String {
        var run = run
        var wicket = wicket
        if (run < 0) {
            run = 0
        }
        if (wicket < 0) {
            wicket = 0
        }
        return if (wicket == 10) {
            run.toString()
        } else {
            "$run/$wicket"
        }
    }
}