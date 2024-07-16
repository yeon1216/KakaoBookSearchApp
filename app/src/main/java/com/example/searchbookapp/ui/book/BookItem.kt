package com.example.searchbookapp.ui.book

import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.searchbookapp.R
import com.example.searchbookapp.data.model.Book
import com.example.searchbookapp.ui.theme.BookTextStyles
import com.example.searchbookapp.ui.theme.Gray200
import com.example.searchbookapp.ui.theme.Gray400
import com.example.searchbookapp.ui.theme.Gray600
import com.example.searchbookapp.ui.theme.Gray900
import com.example.searchbookapp.ui.theme.Red500
import com.example.searchbookapp.ui.utils.formatDate

@Composable
fun BookItem(
    onClickBookItem: (Book) -> Unit,
    onClickFavoriteIcon: (Book) -> Unit,
    modifier: Modifier,
    book: Book
) {

    val painter =
        rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = book.thumbnail.toUri())
                .apply(block = fun ImageRequest.Builder.() {
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_background)
                }).build()
        )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp)
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                onClickBookItem(book)
            },
        elevation = 12.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "book thumbnail",
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DateTimeChip(
    price: Int,
    modifier: Modifier
) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
//                text = context.getString(R.string.publication_date, publicationDate)
                text = context.getString(R.string.book_price, price)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setBackgroundResource(R.drawable.bg_deadline_chip_round)
                }
                setTextColor(resources.getColor(R.color.purple_500, null))
                setTextAppearance(R.style.TEXT_CHIP_MEDIUM)
            }
        },
        modifier = modifier,
        update = {}
    )
}