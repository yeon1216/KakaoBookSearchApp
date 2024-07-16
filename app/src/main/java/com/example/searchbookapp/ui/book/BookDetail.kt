package com.example.searchbookapp.ui.book

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.searchbookapp.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.theme.Black
import com.example.searchbookapp.ui.theme.BookTextStyles
import com.example.searchbookapp.ui.theme.Gray400
import com.example.searchbookapp.ui.theme.Gray600
import com.example.searchbookapp.ui.theme.Gray700
import com.example.searchbookapp.ui.theme.Gray900
import com.example.searchbookapp.ui.theme.Red500
import com.example.searchbookapp.ui.theme.SearchBookTheme
import com.example.searchbookapp.ui.theme.White
import com.example.searchbookapp.ui.utils.formatDate

@Composable
fun BookDetail(
    onClickBackIcon: () -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier,
    book: Book
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Column {
            BookDetailTopBar(
                onClickBackIcon = { onClickBackIcon() },
                modifier = modifier
            )
            BookDetailContents(
                book = book,
                onClickFavoriteIcon = { book -> onClickFavoriteIcon(book) },
                modifier = modifier
            )
        }
    }



}

@Composable
fun BookDetailTopBar(
    onClickBackIcon: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onClickBackIcon()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "arrow_icon",
                    modifier = modifier.size(24.dp),
                    tint = Gray700
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Row(
            modifier = modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.detail_book_title),
                style = BookTextStyles.subTitle1Bold(Black),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }

    }
}

@Composable
fun BookDetailContents(
    book: Book,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier
) {

    val painter =
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = book.thumbnail.toUri())
                .apply(block = fun ImageRequest.Builder.() {
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_background)
                }).build()
        )

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
        ) {
            Box(
                modifier = Modifier
                    .size(116.dp)
            ) {
                Image(
                    painter = painter,
                    contentDescription = "book thumbnail",
                    modifier = Modifier
                        .size(116.dp)
                )
            }
            Row {
                Column(
                    modifier = modifier
                        .weight(1f)
                ) {
                    DateTimeChip(
                        price = book.price,
                        modifier = modifier
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = book.title,
                        style = BookTextStyles.subTitle2Regular(Gray900),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Box(
                    modifier = modifier
                        .size(44.dp)
                ) {
                    IconButton(onClick = {
                        onClickFavoriteIcon(book)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "favorite_icon",
                            modifier = modifier.size(24.dp),
                            tint = if (book.isFavorite) Red500 else Gray400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.contents,
                style = BookTextStyles.subTitle3Regular(Gray600),
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "출판 : ${book.publisher}",
                style = BookTextStyles.subTitle2Regular(Gray900),
                overflow = TextOverflow.Ellipsis
            )
            val formattedDate = formatDate(book.datetime)
            if (formattedDate != null) {
                Text(
                    text = "출판 날짜 : $formattedDate",
                    style = BookTextStyles.subTitle2Regular(Gray900),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewBookDetail() {
    SearchBookTheme {
        BookDetail(
            book = Book(
                    isFavorite = false, // Defaulting isFavorite to false for the preview
                    title = "미움받을 용기",
                    contents = "인간은 변할 수 있고, 누구나 행복해 질 수 있다. 단 그러기 위해서는 ‘용기’가 필요하다고 말한 철학자가 있다. 바로 프로이트, 융과 함께 ‘심리학의 3대 거장’으로 일컬어지고 있는 알프레드 아들러다. 『미움받을 용기』는 아들러 심리학에 관한 일본의 1인자 철학자 기시미 이치로와 베스트셀러 작가인 고가 후미타케의 저서로, 아들러의 심리학을 ‘대화체’로 쉽고 맛깔나게 정리하고 있다. 아들러 심리학을 공부한 철학자와 세상에 부정적이고 열등감 많은",
                    url = "https://search.daum.net/search?w=bookpage&bookId=1467038&q=%EB%AF%B8%EC%9B%80%EB%B0%9B%EC%9D%84+%EC%9A%A9%EA%B8%B0",
                    isbn = "8996991341 9788996991342",
                    datetime = "2014-11-17T00:00:00.000+09:00",
                    authors = listOf("기시미 이치로", "고가 후미타케"),
                    publisher = "인플루엔셜",
                    translators = listOf("전경아"),
                    price = 14900,
                    salePrice = 13410,
                    thumbnail = "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1467038",
                    status = "정상판매"
                ),
            onClickBackIcon = {},
            onClickFavoriteIcon = {},
            modifier = Modifier
        )
    }
}
