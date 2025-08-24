package com.example.ED_P1_Grupo02.Vista;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.ED_P1_Grupo02.Controlador.ControladorVuelo;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;
import com.example.ED_P1_Grupo02.Tdas.Edge;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Tdas.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GrafoView extends View {

    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Paint paintNodo;
    private Paint paintArista;
    private Paint paintTexto;

    private Map<Vertex<Aeropuerto, Vuelo>, Float[]> posiciones = new HashMap<>();

    public GrafoView(Context context) {
        super(context);
        init();
    }

    public GrafoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public GraphAL<Aeropuerto, Vuelo> getGrafo() {
        return grafo;
    }
    private void init() {
        paintNodo = new Paint();
        paintNodo.setColor(Color.YELLOW);
        paintNodo.setStyle(Paint.Style.FILL);
        paintNodo.setAntiAlias(true);

        paintArista = new Paint();
        paintArista.setColor(Color.BLACK);
        paintArista.setStrokeWidth(4);

        paintTexto = new Paint();
        paintTexto.setColor(Color.BLACK);
        paintTexto.setTextSize(35);
        paintTexto.setAntiAlias(true);
    }

    public void setGrafo(GraphAL<Aeropuerto, Vuelo> grafo) {
        this.grafo = grafo;
        // Agrega un observador para asegurar que la vista tiene dimensiones
        if (getWidth() == 0 && getHeight() == 0) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Elimina el observador para que no se ejecute varias veces
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    asignarPosiciones();
                    invalidate();
                }
            });
        } else {
            asignarPosiciones();
            invalidate();
        }
    }

    private void asignarPosiciones() {
        if (grafo == null) return;
        posiciones.clear();

        int width = getWidth();
        int height = getHeight();

        if (width == 0) width = 800;
        if (height == 0) height = 1200;

        // Paso 1: encontrar nodo con mayor grado total
        Vertex<Aeropuerto, Vuelo> nodoCentral = null;
        int maxGrado = -1;
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            int grado = grafo.gradoTotal(v.getContent()); // <-- usamos V content
            if (grado > maxGrado) {
                maxGrado = grado;
                nodoCentral = v;
            }
        }

        if (nodoCentral == null) return; // prevención por si grafo está vacío

        // Paso 2: asignar posición fija al nodo central (centro)
        posiciones.put(nodoCentral, new Float[]{width / 2f, height / 2f});

        // Paso 3: distribuir los demás nodos en círculo alrededor
        int totalNodos = grafo.getVertices().size() - 1; // excluyendo el central
        double radio = Math.min(width, height) / 3.0; // radio del círculo

        int i = 0;
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            if (v.equals(nodoCentral)) continue;

            double angulo = 2 * Math.PI * i / totalNodos;
            float x = (float) (width / 2 + radio * Math.cos(angulo));
            float y = (float) (height / 2 + radio * Math.sin(angulo));

            posiciones.put(v, new Float[]{x, y});
            i++;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (grafo == null) return;

        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            Float[] posInicio = posiciones.get(v);
            if (posInicio == null) continue;

            LinkedList<Edge<Vuelo, Aeropuerto>> edges = v.getEdges();
            for (Edge<Vuelo, Aeropuerto> edge : edges) {
                Vertex<Aeropuerto, Vuelo> destino = edge.getTarget();
                Float[] posDestino = posiciones.get(destino);
                if (posDestino == null) continue;

                // Verificar si existe arista inversa (destino -> v)
                boolean existeInversa = false;
                for (Edge<Vuelo, Aeropuerto> invEdge : destino.getEdges()) {
                    if (invEdge.getTarget().equals(v)) {
                        existeInversa = true;
                        break;
                    }
                }

                // Dibujar la arista con flecha y desplazamiento si bidireccional
                drawEdgeWithArrow(canvas, posInicio, posDestino, existeInversa, false);

                // Dibujar texto en la línea
                float midX = (posInicio[0] + posDestino[0]) / 2;
                float midY = (posInicio[1] + posDestino[1]) / 2;
                String texto = edge.getData().getKilometros() + " km";
                canvas.drawText(texto, midX, midY, paintTexto);
            }
        }

        // Dibujar nodos igual que antes
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            Float[] pos = posiciones.get(v);
            if (pos == null) continue;

            canvas.drawCircle(pos[0], pos[1], 50, paintNodo);
            String codigo = v.getContent().getCodigoIATA();
            canvas.drawText(codigo, pos[0] - 30, pos[1] + 10, paintTexto);
        }
    }
    private void drawEdgeWithArrow(Canvas canvas, Float[] start, Float[] end, boolean isBidirectional, boolean drawInverse) {
        float offset = 15f; // separación de líneas paralelas
        float nodeRadius = 40f; // radio del nodo (igual que usas en onDraw)

        float dx = end[0] - start[0];
        float dy = end[1] - start[1];
        float length = (float) Math.sqrt(dx * dx + dy * dy);

        // Vector normalizado
        float nx = dx / length;
        float ny = dy / length;

        float px = -dy / length;
        float py = dx / length;

        float sx, sy, ex, ey;

        if (isBidirectional) {
            if (drawInverse) {
                sx = start[0] + px * offset;
                sy = start[1] + py * offset;
                ex = end[0] + px * offset;
                ey = end[1] + py * offset;
            } else {
                sx = start[0] - px * offset;
                sy = start[1] - py * offset;
                ex = end[0] - px * offset;
                ey = end[1] - py * offset;
            }
        } else {
            sx = start[0];
            sy = start[1];
            ex = end[0];
            ey = end[1];
        }


        float arrowEndX = ex - nx * nodeRadius;
        float arrowEndY = ey - ny * nodeRadius;


        float lineStartX = sx + nx * nodeRadius;
        float lineStartY = sy + ny * nodeRadius;


        canvas.drawLine(lineStartX, lineStartY, arrowEndX, arrowEndY, paintArista);


        drawArrowHead(canvas, lineStartX, lineStartY, arrowEndX, arrowEndY);
    }


    private void drawArrowHead(Canvas canvas, float fromX, float fromY, float toX, float toY) {
        float arrowSize = 30;
        float angle = (float) Math.atan2(toY - fromY, toX - fromX);

        float x1 = (float) (toX - arrowSize * Math.cos(angle - Math.PI / 6));
        float y1 = (float) (toY - arrowSize * Math.sin(angle - Math.PI / 6));
        float x2 = (float) (toX - arrowSize * Math.cos(angle + Math.PI / 6));
        float y2 = (float) (toY - arrowSize * Math.sin(angle + Math.PI / 6));

        canvas.drawLine(toX, toY, x1, y1, paintArista);
        canvas.drawLine(toX, toY, x2, y2, paintArista);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX(), y = event.getY();
            Vertex<Aeropuerto, Vuelo> seleccionado = detectarNodo(x, y);
            if (seleccionado != null && getContext() instanceof android.app.Activity) {
                Aeropuerto aeropuerto = seleccionado.getContent();
                int gradoSalida = grafo.gradoSalida(aeropuerto);
                int gradoEntrada = grafo.gradoEntrada(aeropuerto);
                int gradoTotal = grafo.gradoTotal(aeropuerto);
                ControladorVuelo controladorVuelo = new ControladorVuelo();
                LinkedList<String> aerolineas = controladorVuelo.obtenerAerolineasOrdenadas(aeropuerto);

                Intent intent = new Intent((Activity) getContext(), DetalleConexionesActivity.class);
                intent.putExtra("codigoIATA", aeropuerto.getCodigoIATA());
                intent.putExtra("nombreAeropuerto", aeropuerto.getNombre());
                intent.putExtra("gradoSalida", gradoSalida);
                intent.putExtra("gradoEntrada", gradoEntrada);
                intent.putExtra("grado", gradoTotal);
                intent.putStringArrayListExtra("aerolineas", new ArrayList<>(aerolineas));

                ((Activity) getContext()).startActivityForResult(intent, 101);
            }
        }
        return true;
    }

    private Vertex<Aeropuerto, Vuelo> detectarNodo(float x, float y) {
        for (Map.Entry<Vertex<Aeropuerto, Vuelo>, Float[]> entry : posiciones.entrySet()) {
            Float[] pos = entry.getValue();
            float distancia = (float) Math.sqrt(Math.pow(x - pos[0], 2) + Math.pow(y - pos[1], 2));
            if (distancia <= 40) { // Radio del nodo
                return entry.getKey();
            }
        }
        return null;
    }
}
