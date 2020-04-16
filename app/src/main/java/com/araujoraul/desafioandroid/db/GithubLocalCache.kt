package com.araujoraul.desafioandroid.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.araujoraul.desafioandroid.data.model.PullRequest
import com.araujoraul.desafioandroid.data.model.Repository
import java.util.concurrent.Executor

class GithubLocalCache(
        private val repoDao: RepoDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insert(repos: List<Repository>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "inserting ${repos.size} repos")
            repoDao.insertRepos(repos)
            insertFinished()
        }
    }


    /**
     * Insert a list of pull requests in the database, on a background thread.
     */
    fun insertPull(pulls: List<PullRequest>, insertFinished: () -> Unit ){
        ioExecutor.execute {
            Log.d("GithubLocalCache PullRequest", "inserting ${pulls.size} pulls")
            repoDao.insertPulls(pulls)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the GitHub API behavior and allow
     * any characters between the words.
     * @param name repository name
     */
    fun reposByName(name: String): LiveData<List<Repository>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }

}