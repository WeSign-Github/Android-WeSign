package com.wesign.wesign.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.wesign.wesign.BuildConfig
import com.wesign.wesign.R
import com.wesign.wesign.core.SIGN_IN_REQUEST
import com.wesign.wesign.core.SIGN_UP_REQUEST
import com.wesign.wesign.core.SessionManager
import com.wesign.wesign.core.TIMEOUT
import com.wesign.wesign.data.FirebaseAuthRepositoryImpl
import com.wesign.wesign.data.WeSignRepositoryImpl
import com.wesign.wesign.data.remote.WeSignApiService
import com.wesign.wesign.domain.FirebaseAuthRepository
import com.wesign.wesign.domain.WeSignRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) = SessionManager(context)

    @Provides
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)

        builder.addNetworkInterceptor(
            Interceptor { chain: Interceptor.Chain ->
                val token = runBlocking {
                    sessionManager.getToken().first()
                }

                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            },
        )
        builder.addInterceptor(logging)

        return builder.build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_WESIGN_BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideWeSignApi(retrofit: Retrofit): WeSignApiService =
        retrofit.create(WeSignApiService::class.java)

    @Provides
    fun proviceWeSignRepository(weSignRepositoryImpl: WeSignRepositoryImpl): WeSignRepository = weSignRepositoryImpl


    @Provides
    fun provideOneTapClient(@ApplicationContext context: Context) =
        Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.FIREBASE_WEB_CLIENT_ID))
                .setFilterByAuthorizedAccounts(true)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.FIREBASE_WEB_CLIENT_ID))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()


    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.FIREBASE_WEB_CLIENT_ID))
        .requestEmail()
        .build()

    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

    @Provides
    fun provideFirebaseAuthRepository(firebaseAuthRepositoryImpl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository =
        firebaseAuthRepositoryImpl

}