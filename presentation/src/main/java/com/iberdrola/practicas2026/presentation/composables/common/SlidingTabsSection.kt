package com.iberdrola.practicas2026.presentation.composables.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import com.iberdrola.practicas2026.presentation.ui.theme.BrandGreen
import com.iberdrola.practicas2026.presentation.ui.theme.Dimens
import com.iberdrola.practicas2026.presentation.ui.theme.DividerColor
import com.iberdrola.practicas2026.presentation.ui.theme.TextMain
import com.iberdrola.practicas2026.presentation.ui.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun SlidingTabsSection(
    pagerState: androidx.compose.foundation.pager.PagerState,
    tabs: List<String>,
    coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    val density = LocalDensity.current
    val tabPositions = remember { mutableStateListOf<Pair<Dp, Dp>>() }

    if (tabPositions.isEmpty()) {
        repeat(tabs.size) { tabPositions.add(0.dp to 0.dp) }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomStart
    ) {
        HorizontalDivider(
            thickness = Dimens.StrokeThick,
            color = DividerColor
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.SpacingM)
        ) {

            // Tabs
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingM)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = pagerState.currentPage == index

                    Box(
                        modifier = Modifier
                            .onGloballyPositioned { coords ->
                                val width = with(density) { coords.size.width.toDp() }
                                val x = with(density) {
                                    coords.parentLayoutCoordinates?.localPositionOf(coords, androidx.compose.ui.geometry.Offset.Zero)?.x?.toDp() ?: 0.dp
                                }
                                tabPositions[index] = width to x
                            }
                            .clip(RoundedCornerShape(topStart = Dimens.SpacingS, topEnd = Dimens.SpacingS))
                            .clickable {
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            }
                            .padding(bottom = 12.dp, top = Dimens.SpacingS)
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) TextMain else TextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = Dimens.SpacingS)
                        )
                    }
                }
            }

            if (tabPositions.size > pagerState.currentPage) {
                val currentPos = tabPositions[pagerState.currentPage]

                val animatedWidth by animateDpAsState(
                    targetValue = currentPos.first,
                    animationSpec = spring(stiffness = 500f),
                    label = "width"
                )
                val animatedOffsetX by animateDpAsState(
                    targetValue = currentPos.second,
                    animationSpec = spring(stiffness = 500f),
                    label = "offset"
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = animatedOffsetX)
                        .width(animatedWidth)
                        .height(Dimens.SpacingXS)
                        .background(BrandGreen)
                )
            }
        }
    }
}