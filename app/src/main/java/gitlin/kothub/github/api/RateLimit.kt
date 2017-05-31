package gitlin.kothub.github.api

import gitlin.kothub.github.api.data.RateLimit
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


object ApiRateLimit {
    private val rate: BehaviorSubject<RateLimit> = BehaviorSubject.create()

    fun observable (): Observable<RateLimit> = rate

    fun updateRate(rate: RateLimit) {
        this.rate.onNext(rate)
    }
}