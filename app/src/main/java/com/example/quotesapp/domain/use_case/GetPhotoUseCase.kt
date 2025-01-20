package com.example.quotesapp.domain.use_case

import com.example.quotesapp.common.Resource
import com.example.quotesapp.common.utils.Orientation
import com.example.quotesapp.data.remote.dto.toPhoto
import com.example.quotesapp.domain.model.Photo
import com.example.quotesapp.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetPhotoUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    operator fun invoke(): Flow<Resource<Photo>> = flow {
        try {
            emit(Resource.Loading<Photo>())
            val photo = repository.getPhoto(Orientation.PORTRAIT.value).toPhoto()
            emit(Resource.Success<Photo>(photo))
        } catch (e: HttpException) {
            emit(Resource.Error<Photo>(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<Photo>("Couldn't reach server. Check your Internet connection"))
        }
    }
}