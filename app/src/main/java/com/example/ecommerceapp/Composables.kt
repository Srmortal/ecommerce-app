import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    icon: Int,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onNext: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier
) {

    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {

        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
                onDone = { onNext?.invoke() }
            ),
            visualTransformation =
                if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedPlaceholderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedPlaceholderColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = { Text(placeholder, color = Color(0xFF8CADEF)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(18.dp)
                )
            }
        )
    }
}

@Composable
fun InputField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onNext: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier
) {

    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {

        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = imeAction,
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
                onDone = { onNext?.invoke() }
            ),
            visualTransformation =
                if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedPlaceholderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                unfocusedPlaceholderColor = Color.White
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = { Text(placeholder, color = Color(0xFF8CADEF)) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    modifier = Modifier.size(18.dp)
                )
            }
        )
    }
}

@Composable
fun Logo(size: Dp, mainColor: Color, secondColor: Color) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(
                RoundedCornerShape(
                    topEnd = size / 2,
                    bottomEnd = 2.dp,
                    topStart = size / 2,
                    bottomStart = size / 2,
                )
            )
            .background(color = mainColor)
    ) {
        Text(
            "S",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = size * 1 / 6, x = size * 4 / 35),
            color = secondColor,
            style = MaterialTheme.typography.headlineMedium,
            fontSize = size.value.sp * 6 / 7,
        )
    }
}

@Composable
fun ErrorText(message: String) {
    Text(
        message,
        color = Color(0xFFFFA726),
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    )
}

@Composable
fun InfoText(
    strings: List<String>,
    tags: List<String>,
    onTagClick: Map<String, () -> Unit>
) {
    val pressedStates = remember { tags.associateWith { mutableStateOf(false) } }

    var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

    var tagIndex = 0
    var isBold = false

    val annotated = buildAnnotatedString {
        for (string in strings) {
            if (isBold) {
                val tag = tags[tagIndex]
                tagIndex++

                val isPressed = pressedStates[tag]?.value ?: false

                pushStringAnnotation(tag = tag, annotation = tag)
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        color = if (isPressed) MaterialTheme.colorScheme.secondary else Color.White
                    )
                ) {
                    append(string)
                }
                pop()
            } else {
                withStyle(SpanStyle(color = Color.White)) {
                    append("$string ")
                }
            }

            isBold = !isBold
        }
    }

    BasicText(
        text = annotated,
        style = MaterialTheme.typography.labelMedium,
        onTextLayout = { textLayout = it },
        modifier = Modifier
            .padding(15.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val idx =
                            textLayout?.getOffsetForPosition(offset) ?: return@detectTapGestures
                        val ann = annotated.getStringAnnotations(idx, idx).firstOrNull()
                            ?: return@detectTapGestures

                        pressedStates[ann.tag]?.value = true

                        try {
                            awaitRelease()
                        } finally {
                            pressedStates[ann.tag]?.value = false
                        }
                    },
                    onTap = { offset ->
                        val idx =
                            textLayout?.getOffsetForPosition(offset) ?: return@detectTapGestures
                        val ann = annotated.getStringAnnotations(idx, idx).firstOrNull()
                            ?: return@detectTapGestures

                        onTagClick[ann.tag]?.invoke()
                    }
                )
            }
    )
}
