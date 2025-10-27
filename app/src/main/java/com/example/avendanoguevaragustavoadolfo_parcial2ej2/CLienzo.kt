package com.example.avendanoguevaragustavoadolfo_parcial2ej2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class CLienzo : View {
    private var x = 0f
    private var y = 0f
    private var radio: Float? = null

    private var segundos: Int = 0
    private var minutos: Int = 0
    private var puntaje: Int = 0
    private var colision: Int = 0
    private var turnoIzquierda = false
    private var reiniciar = false

    private var drawColor: Int = 0
    private var paintBalon = Paint()
    private var paintObstaculos = Paint()

    private var paintTexto: Paint = Paint()
    private var paintBorde: Paint = Paint()
    private var paintFondo: Paint = Paint()
    private var paintFondo_aviso: Paint = Paint()
    private val esquinas = 10f

    //Mapa
    private lateinit var mapa: Array< Array<Int> >
    private var ancho = 0
    private var alto = 0

    constructor(context: Context?) : super(context) {
        inicializa()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        inicializa()
    }

    private fun inicializa() {
        //Balón
        drawColor = ResourcesCompat.getColor(resources, R.color.black, null)
        paintBalon.color = drawColor

        //Obstáculos
        val gradiente = ContextCompat.getDrawable(context, R.drawable.degradado_obstaculos) as GradientDrawable
        val colores = gradiente.colors
        val positions = floatArrayOf(0f, 0.5f, 1f)

        val linearGradient = LinearGradient(
            2000f, 1000f, width.toFloat(), height.toFloat(), //Modifica los valores del gradiente
            colores!!, // startColor, centerColor, endColor
            positions, Shader.TileMode.CLAMP
        )
        paintObstaculos.shader = linearGradient

        //Texto
        paintTexto.color = Color.BLACK
        paintTexto.isAntiAlias = true
        paintTexto.isDither = true
        paintTexto.style = Paint.Style.FILL
        paintTexto.textSize = 50f
        paintTexto.typeface = Typeface.DEFAULT_BOLD
        //Borde
        paintBorde.color = Color.BLACK
        paintBorde.isAntiAlias = true
        paintBorde.style = Paint.Style.STROKE
        paintBorde.strokeWidth = 5f
        //Fondo del Texto
        //paintFondo.color = Color.parseColor("#D8EAF5")
        paintFondo.color = Color.LTGRAY
        paintFondo.isAntiAlias = true
        paintFondo.style = Paint.Style.FILL
        //Fondo del Texto Aviso
        paintFondo_aviso.color = Color.YELLOW
        paintFondo_aviso.isAntiAlias = true
        paintFondo_aviso.style = Paint.Style.FILL


        mapa = arrayOf(
                arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
                arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
                arrayOf(1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1),
                arrayOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1),
                arrayOf(1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1),
                arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
                arrayOf(1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1),
                arrayOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1),
                arrayOf(1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1),
                arrayOf(1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1),
                arrayOf(1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1),
                arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1),
                arrayOf(1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1),
                arrayOf(1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1),
                arrayOf(1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1),
                arrayOf(1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1),
                arrayOf(1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1),
                arrayOf(1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1),
                arrayOf(1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1),
                arrayOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1),
                arrayOf(1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1),
                arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
                arrayOf(1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1),
                arrayOf(1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1),
                arrayOf(1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1),
                arrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
                arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        )
        //Cronómetro
        val hilo: Thread = object : Thread() {
            @Synchronized
            override fun run() {
                while(true) {
                    try {
                        if(!reiniciar) {
                            sleep(1000)
                            if(segundos < 60) segundos += 1
                            else {
                                minutos += 1
                                segundos = 0
                            }
                            invalidate()
                        }
                    }
                    catch(e: InterruptedException) {
                    }
                }
            }
        }
        hilo.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        ancho = this.width / mapa[0].size
        alto = this.height / mapa.size
        radio = ancho.toFloat()/5

        //Balón
        canvas.drawCircle(x, y, radio!!, paintBalon)
        val izquierdaBalon = x-radio!!
        val derechaBalon = x+radio!!
        val arribaBalon = y-radio!!
        val abajoPelota = y+radio!!

        //Obstáculos
        var xObstaculo = 0f; var yObstaculo = 0f

        var izquierdaObstaculo = xObstaculo
        var derechaObstaculo = xObstaculo+ancho
        var arribaObstaculo = yObstaculo
        var abajoObstaculo = yObstaculo+alto

        for(i in 0 until mapa.size) {
            for(j in 0 until mapa[i].size) {

                if(mapa[i][j] === 1) {
                    //Dibujar
                    canvas.drawRect(xObstaculo, yObstaculo, xObstaculo+ancho, yObstaculo+alto, paintObstaculos)

                    izquierdaObstaculo = xObstaculo
                    derechaObstaculo = xObstaculo+ancho
                    arribaObstaculo = yObstaculo
                    abajoObstaculo = yObstaculo+alto

                    if(derechaBalon > izquierdaObstaculo && izquierdaBalon < derechaObstaculo &&
                        abajoPelota > arribaObstaculo && arribaBalon < abajoObstaculo) {
                        //Colisión del Balón con la parte de Arriba del Obstáculo
                        if(abajoPelota > arribaObstaculo && arribaBalon < arribaObstaculo) {
                            colision = 1
                        }
                        //Colisión del Balón con la parte de Abajo del Obstáculo
                        if(arribaBalon < abajoObstaculo && abajoPelota > abajoObstaculo) {
                            colision = 2
                        }
                        //Colisión del Balón con la parte Derecha del Obstáculo
                        if(izquierdaBalon < derechaObstaculo && derechaBalon > derechaObstaculo) {
                            colision = 3
                        }
                        //Colisión del Balón con la parte Izquierda del Obstáculo
                        if(derechaBalon > izquierdaObstaculo && izquierdaBalon < izquierdaObstaculo) {
                            colision = 4
                        }
                    }
                }

                //Gol a la Porteria Izquierda
                if(mapa[i][j] == 2 && turnoIzquierda) {
                    if(derechaBalon > xObstaculo && izquierdaBalon < xObstaculo+ancho &&
                        abajoPelota > yObstaculo && arribaBalon < yObstaculo+alto) {
                        puntaje++
                        turnoIzquierda = false
                    }
                }

                //Gol a la Porteria Derecha
                if(mapa[i][j] == 3 && !turnoIzquierda) {
                    if(derechaBalon > xObstaculo && izquierdaBalon < xObstaculo+ancho &&
                        abajoPelota > yObstaculo && arribaBalon < yObstaculo+alto) {
                        puntaje++
                        turnoIzquierda = true
                    }
                }

                xObstaculo = xObstaculo + ancho
            }
            xObstaculo = 0f
            yObstaculo = yObstaculo + alto
        }

        //Colisiones
        if(colision != 0) {
            //Colisión del Balón con la parte de Arriba del Obstáculo
            if(colision == 1) {
                y = y - radio!!
            }
            //Colisión del Balón con la parte de Abajo del Obstáculo
            if(colision == 2) {
                y = y + radio!!
            }
            //Colisión del Balón con la parte Derecha del Obstáculo
            if(colision == 3) {
                x = x + radio!!
            }
            //Colisión del Balón con la parte Izquierda del Obstáculo
            if(colision == 4) {
                x = x - radio!!
            }
            colision = 0 //Sin colisión
        }
        //Centrar Balón
        if(reiniciar) {
            x = this.width/2f
            y = this.height/2f
            reiniciar = false
        }

        //Texto Puntaje
        canvas.drawRoundRect(0f, 0f, 290f, 50f, esquinas, esquinas, paintBorde)
        canvas.drawRoundRect(0f, 0f, 290f, 50f, esquinas, esquinas, paintFondo)
        canvas.drawText("Puntaje: " + puntaje, 10f, 40f, paintTexto)

        //Cronómetro
        var tiempo: String
        if(segundos < 10) {
            tiempo = "Tiempo: $minutos:0$segundos"
        }
        else {
            tiempo = "Tiempo: $minutos:$segundos"
        }

        val anchoTexto = paintTexto.measureText(tiempo)
        val altoTexto = paintTexto.textSize
        val padding = 20f

        val xFondo = (width-anchoTexto)/2 //Centrar Horizontal
        val yFondo = 40f //Altura
        val x1Fondo = xFondo-padding; val x2Fondo = xFondo+anchoTexto+padding
        val y1Fondo = yFondo-altoTexto-padding/2; val y2Fondo = yFondo+padding/2

        canvas.drawRoundRect(x1Fondo, y1Fondo, x2Fondo, y2Fondo, esquinas, esquinas, paintBorde) //Borde
        canvas.drawRoundRect(x1Fondo, y1Fondo, x2Fondo, y2Fondo, esquinas, esquinas, paintFondo) //Fondo
        canvas.drawText(tiempo, xFondo, yFondo, paintTexto)

        //Botón Reiniciar
        canvas.drawRoundRect(this.width-240f, 0f, this.width.toFloat(), 50f, esquinas, esquinas, paintBorde)
        canvas.drawRoundRect(this.width-240f, 0f, this.width.toFloat(), 50f, esquinas, esquinas, paintFondo)
        canvas.drawText("Reiniciar", this.width-220f, 40f, paintTexto)

        //Turno de Porteria
        if(turnoIzquierda) {
            canvas.drawRoundRect(390f, 0f, 800f, 50f, esquinas, esquinas, paintBorde)
            canvas.drawRoundRect(390f, 0f, 800f, 50f, esquinas, esquinas, paintFondo_aviso)
            canvas.drawText("Turno: Izquierda", 400f, 40f, paintTexto)
        }
        else {
            canvas.drawRoundRect(this.width-760f, 0f, this.width-380f, 50f, esquinas, esquinas, paintBorde)
            canvas.drawRoundRect(this.width-760f, 0f, this.width-380f, 50f, esquinas, esquinas, paintFondo_aviso)
            canvas.drawText("Turno: Derecha", this.width-750f, 40f, paintTexto)
        }

        invalidate()
    }

    //Posiciones X y Y
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //Obtenemos X y Y
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        //Validamos si se presiona, se mueve o se libera...
        if(event.action == MotionEvent.ACTION_DOWN) {
            if(motionTouchEventX >= this.width - 240f && motionTouchEventX <= this.width &&
               motionTouchEventY >= 0f && motionTouchEventY <= 50f) {
                segundos = 0; minutos = 0
                puntaje = 0
                reiniciar = true
                invalidate()  //Redibujar la vista
            }
        }
        if(event.action == MotionEvent.ACTION_MOVE) {
        }
        if(event.action == MotionEvent.ACTION_UP) {
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        x = w/2f
        y = h/2f
    }

    fun setPosicionPelota(x: Float, y: Float) {
        if(colision == 0) {
            this.x += x*1.2f
            this.y += y*1.2f
        }

        //Balón dentro de los límites de la pantalla
        this.x = this.x.coerceIn(radio, width-radio!!)
        this.y = this.y.coerceIn(radio, height-radio!!)

        invalidate() //Redibujar la vista
    }
}