package com.rafsan.classifiedsapp.viewmodel

import com.google.common.truth.Truth.assertThat
import com.rafsan.classifiedsapp.ui.detail.DetailViewModel
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        viewModel = DetailViewModel()
    }

    @Test
    fun `test convert date to format`() {
        val result = viewModel.convertDate("2019-02-24 04:04:17.566515")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("Feb 24, 2019 04:13 AM")
    }

    @Test
    fun `test convert date exception`() {
        val dateStr = "2021-05-12 04:04:17"
        val result = viewModel.convertDate(dateStr)
        assertThat(result).isNotNull()
        //returns same in case of exception
        assertThat(result).isEqualTo(dateStr)
    }

    @Test
    fun `test get image url returns url`() {
        val urls = listOf("a.jpg", "b.jpg")
        val url = viewModel.getImageUrl(urls)
        assertThat(url).isNotNull()
        assertThat(url).isEqualTo("a.jpg")
    }

    @Test
    fun `test get image url returns null`() {
        val urls = listOf<String>()
        val url = viewModel.getImageUrl(urls)
        assertThat(url).isNull()
    }

    @After
    fun release() {

    }
}