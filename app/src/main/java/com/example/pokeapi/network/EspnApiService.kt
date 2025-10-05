package com.example.pokeapi.network

import com.example.pokeapi.model.espn.NewsResponse
import com.example.pokeapi.model.espn.ScoreboardResponse
import com.example.pokeapi.model.espn.Team
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface EspnApiService {
    // News by league
    @GET("apis/site/v2/sports/soccer/{league}/news")
    fun getLeagueNews(@Path("league") league: String): Call<NewsResponse>



    // Scoreboard by league and date yyyymmdd
    @GET("apis/site/v2/sports/soccer/{league}/scoreboard")
    fun getLeagueScoreboard(
        @Path("league") league: String,
        @Query("dates") yyyymmdd: String
    ): Call<ScoreboardResponse>

    // Teams by league (used for search)
    @GET("apis/site/v2/sports/soccer/{league}/teams")
    fun getLeagueTeams(@Path("league") league: String): Call<TeamsResponse>

    // Team details (roster)
    @GET("apis/site/v2/sports/soccer/{league}/teams/{teamId}")
    fun getTeamDetails(
        @Path("league") league: String,
        @Path("teamId") teamId: String
    ): Call<TeamDetailResponse>

    // Dynamic news endpoint (if required elsewhere)
    @GET
    fun getNewsDynamic(@Url relativeUrl: String): Call<NewsResponse>
}

// Minimal response tree for /teams endpoint.
// We reuse your existing model Team (from Scoreboard models) at the leaf.
data class TeamsResponse(val sports: List<SportX>?)
data class SportX(val leagues: List<LeagueX>?)
data class LeagueX(val teams: List<TeamX>?)
data class TeamX(val team: Team?)

data class TeamDetailResponse(@SerializedName("team") val team: TeamDetail?)
data class TeamDetail(
    @SerializedName("id") val id: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("athletes") val athletes: AthletesContainer?
)

data class AthletesContainer(
    @SerializedName("items") val items: List<AthleteItem>?,
    @SerializedName("athletes") val groups: List<AthleteGroup>?,
    @SerializedName("groups") val legacyGroups: List<AthleteGroup>?
)

data class AthleteGroup(
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("items") val items: List<AthleteItem>?
)

data class AthleteItem(
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("shortName") val shortName: String?,
    @SerializedName("jersey") val jersey: String?,
    @SerializedName("position") val position: AthletePosition?
)

data class AthletePosition(
    @SerializedName("displayName") val displayName: String?,
    @SerializedName("abbreviation") val abbreviation: String?
)

// Singleton
val espnApi: EspnApiService by lazy {
    EspnRetrofit.retrofit.create(EspnApiService::class.java)
}
