package com.example.farmlinkapp1.ui.for_buyer.saleItems_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.farmlinkapp1.common.AsyncImageLoader
import com.example.farmlinkapp1.common.SaleItemCard
import com.example.farmlinkapp1.common.SearchFeature
import com.example.farmlinkapp1.model.Item
import org.mongodb.kbson.ObjectId

@Composable
fun SaleItemsScreen(
    itemId: ObjectId,
    onClick: (ObjectId) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: SaleItemsViewModel = viewModel()
    val scrollState = rememberLazyListState()
    val saleItemsList by viewModel.getSellersByItemId(itemId).collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val item = viewModel.getItemById(itemId)

    LaunchedEffect(remember { derivedStateOf { scrollState.firstVisibleItemScrollOffset } }) {
        viewModel.updateMapHeight(scrollState.firstVisibleItemScrollOffset)
    }

    SearchFeature(textFieldValue = "", onValueChange = {})

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState
    ) {

        item {
            PageHeader(item)
        }
        item {
            ShrinkableMapBox(viewModel.mapHeight)
        }

        if (saleItemsList.isNotEmpty()) {
            item {
                SaleItemCard(
                    title = "Item Name Here",
                    saleItem = saleItemsList[0],
                    onClick = { onClick(saleItemsList[0]._id) },
                    cardColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            }

            if (saleItemsList.size > 1) {
                for (i in 1..<saleItemsList.size) {
                    if (saleItemsList[i].active) {
                        item {
                            SaleItemCard(
                                title = saleItemsList[i].seller?.user?.name!!,
                                saleItem = saleItemsList[i],
                                onClick = { onClick(saleItemsList[i]._id) },
                                isRecommended = true
                            )
                        }
                    }
                }
            }
        } else {
            item {
                Text(
                    text = "No Items Found!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PageHeader(
    item: Item,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImageLoader(
            imageUrl = item.imageUrl,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(32.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "MSP: ₹${item.msp}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "AVG. PRICE: ₹100",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ShrinkableMapBox(mapHeight: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(mapHeight)
            .border(1.dp, MaterialTheme.colorScheme.onSurface)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
    ) {
        Text(
            text = "Map will be here",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
